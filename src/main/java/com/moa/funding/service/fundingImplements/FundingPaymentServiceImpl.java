package com.moa.funding.service.fundingImplements;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.exception.FundingPeriodException;
import com.moa.funding.exception.RewardLimitException;
import com.moa.funding.exception.RewardStockException;
import com.moa.funding.mapper.FundingPaymentMapper;
import com.moa.funding.repository.FundingManagementRepositoryCustom;
import com.moa.funding.repository.FundingSelectRepositoryCustom;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.RewardStockCache;
import com.moa.funding.service.portone.PortOneService;
import com.moa.funding.service.RewardService;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundingPaymentServiceImpl implements FundingPaymentService {
	private final PortOneService iamportOneService;
	private final FundingOrderRepository fundingOrderRepository;
	private final FundingContributionRepository fundingContributionRepository;
	private final FundingRepository fundingRepository;
	private final UserRepository userRepository;
	private final RewardRepository rewardRepository;
	private final RewardService rewardService;
	private final FundingSelectRepositoryCustom fundingSelectRepositoryCustom;
	private final RewardStockCache rewardStockCache;
	private final FundingManagementRepositoryCustom fundingManagementRepositoryCustom;


	@Transactional
	@Scheduled(cron = "0 0/5 * * * *")//5분마다 실행
	// @Scheduled(cron = "0 * * * * *") //1분마다 실행 태스트 용
	public void cancelExpiredFundingOrders() {
		// Timestamp cutoffTime = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 10); // 10분 전
		// Timestamp cutoffTime = new Timestamp(System.currentTimeMillis() - 1000 * 60); // 1분 전 테스트용 코드
		log.info("만료된 펀딩 주문 처리 중...");
		Timestamp cutoffTime = new Timestamp(System.currentTimeMillis() - 1000 * 60 * 5); // 5분 전

		List<FundingOrder> expiredOrders = fundingSelectRepositoryCustom.findPendingOrdersOlderThan(cutoffTime);

		for (FundingOrder order : expiredOrders) {
			cancelFundingContribution(order);
			fundingOrderRepository.delete(order);
		}

		log.info("만료된 펀딩 주문 처리 완료 - 총 {}개 주문 삭제", expiredOrders.size());
	}


	@Override
	@Transactional
	public void prepareFundingOrder(PaymentRequest paymentRequest) {
		log.info("결제 준비 중 - PaymentRequest: {}", paymentRequest);
		// Step 1: 펀딩 및 리워드 정보 확인
		Funding funding = getFunding(paymentRequest);

		//검증
		validateFundingAndRewards(funding, paymentRequest.getRewardList());

		// Step 2: 리워드 재고 감소
		for (RewardRequest rewardRequest : paymentRequest.getRewardList()) {
			rewardService.reduceRewardStock(rewardRequest);
		}
		// Step 3: 사용자 조회
		User user = getUser(paymentRequest);

		// Step 4: 펀딩 주문 생성 및 저장
		FundingOrder fundingOrder = createAndSaveFundingOrder(paymentRequest, user);

		// Step 5: 리워드 감소 정보 캐시에 저장
		rewardStockCache.addRewardChanges(fundingOrder.getFundingOrderId(), paymentRequest.getRewardList());
		log.info("결제 준비 완료 - FundingOrder: {}", fundingOrder);
	}


	@Override
	@Transactional
	public void processFundingContribution(String impUid, PaymentRequest paymentRequest) {
		log.info("결제된 펀딩 후원 처리   - impUid: {}, PaymentRequest: {}", impUid, paymentRequest);
		// Step 1: 검증 및 중복 결제 리워드 정보 확인
		validatePayment(impUid, paymentRequest);

		// Step 2: 펀딩 및 리워드 정보 확인
		FundingOrder fundingOrder = getFundingOrder(paymentRequest);

		// Step 3: imUid 업데이트 및 결제 상태 업데이트
		updateFundingOrderAfterSuccess(fundingOrder, impUid);

		// Step 4: 펀딩 조회 및 후원 생성 및 저장
		createAndSaveFundingContribution(paymentRequest, fundingOrder);

		// Step 5: 펀딩의 currentAmount 업데이트
		updateFundingCurrentAmount(paymentRequest);

		fundingManagementRepositoryCustom.validateAndUpdateFundingStatuses();

		log.info("결제된 펀딩 후원 처리 완료 - impUid: {}, PaymentRequest: {}", impUid, paymentRequest);
	}



	private void cancelFundingContribution(FundingOrder order) {
		// 캐시에서 리워드 감소 정보를 조회
		List<RewardRequest> rewardRequests = rewardStockCache.getAndRemoveRewardChanges(order.getFundingOrderId());

		if (rewardRequests != null) {
			for (RewardRequest rewardRequest : rewardRequests) {
				rewardService.restoreRewardStock(rewardRequest);
				log.info("복구된 리워드: {}", rewardRequest);
			}
		}
		// 주문 삭제
		fundingOrderRepository.delete(order);
		log.info("만료된 펀딩 주문 취소 - FundingOrderId: {}", order.getFundingOrderId());
	}



	private void validatePayment(String impUid, PaymentRequest paymentRequest) {
		// Step 1: 중복 결제 확인
		if (fundingOrderRepository.existsByImpUid(impUid)) {
			throw new IllegalStateException("이미 처리된 결제입니다: impUid=" + impUid);
		}
		// Step 2: iamport 결제 검증
		boolean isVerified = iamportOneService.verifyPayment(paymentRequest.getTotalAmount(), impUid);
		if (!isVerified) {
			throw new RuntimeException("결제 검증 실패");
		}
		// Step 3: 리워드 정보 확인
		if (paymentRequest.getRewardList() == null || paymentRequest.getRewardList().isEmpty()) {
			throw new RuntimeException("리워드 정보가 존재하지 않습니다.");
		}
	}

	private void validateFundingAndRewards(Funding funding, List<RewardRequest> rewardRequests) {
		// 현재 UTC 시간
		Instant now = Instant.now();

		// 펀딩 기간 확인 	// 펀딩 시작일 이전 또는 종료일 이후에 결제 요청이 들어온 경우
		if (now.isBefore(funding.getStartDate()) || now.isAfter(funding.getEndDate())) {
			throw new FundingPeriodException("펀딩 기간이 아닙니다.");
		}

		// 리워드 검증
		Reward reward = null;
		for (RewardRequest rewardRequest : rewardRequests) {
			reward = rewardService.getReward(rewardRequest);

			// (1) 가격 검증
			if (isRewardPriceMismatched(rewardRequest, reward)) {
				throw new IllegalStateException("리워드 가격이 서버 데이터와 일치하지 않습니다. - RewardId: " + reward.getRewardId());
			}

			// (3) 구매 제한 검증
			if (reward.getIsLimit() != null && reward.getIsLimit()
				&& rewardRequest.getRewardQuantity() > reward.getLimitQuantity()) {
				throw new RewardLimitException(
					"리워드 구매 수량이 제한을 초과했습니다. - Reward이름 : " + reward.getRewardName() +
						", 제한: " + reward.getLimitQuantity() +
						", 요청 수량: " + rewardRequest.getRewardQuantity());
			}

		}
		log.info("펀딩 및 리워드 검증 완료 - FundingId: {}, RewardRequests: {}", funding.getFundingId(), rewardRequests);

	}


	private FundingOrder createAndSaveFundingOrder(PaymentRequest paymentRequest, User user) {
		Funding funding = getFunding(paymentRequest);
		FundingOrder order = FundingPaymentMapper.toFundingOrder(paymentRequest, user, funding);
		return fundingOrderRepository.save(order);
	}

	private void createAndSaveFundingContribution(PaymentRequest paymentRequest, FundingOrder savedOrder) {
		//Step1: 펀딩 조회
		Funding funding = getFunding(paymentRequest);

		//Step2: 각 리워드별 후원 생성 및 저장
		for (RewardRequest rewardRequest : paymentRequest.getRewardList()) {
			// Step 3: 리워드 조회
			Reward reward = rewardService.getReward(rewardRequest);

			// Step 3.1: 후원 생성 및 저장
			FundingContribution contribution = FundingPaymentMapper.toFundingContribution(rewardRequest, savedOrder,
				funding,
				reward);
			fundingContributionRepository.save(contribution);
		}
	}

	@Override
	public void updateFundingCurrentAmount(PaymentRequest paymentRequest) {
		Funding funding = getFunding(paymentRequest);
		// Step 1: 펀딩의 currentAmount 업데이트
		funding.setCurrentAmount(
			funding.getCurrentAmount().add(BigDecimal.valueOf(paymentRequest.getTotalAmount()))
		);
		fundingRepository.save(funding);
	}

	private Funding getFunding(PaymentRequest paymentRequest) {
		return fundingRepository.findById(paymentRequest.getFundingId())
			.orElseThrow(() -> new IllegalArgumentException("펀딩이 존재하지 않습니다."));
	}

	private User getUser(PaymentRequest paymentRequest) {
		return userRepository.findByUsername(paymentRequest.getUserName())
			.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
	}

	private FundingOrder getFundingOrder(PaymentRequest paymentRequest) {
		return fundingOrderRepository.findByMerchantUid(paymentRequest.getMerchantUid())
			.orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다."));
	}


	private  boolean isRewardPriceMismatched(RewardRequest rewardRequest, Reward reward) {
		return !reward.getRewardPrice().equals(rewardRequest.getRewardPrice());
	}


	private void updateFundingOrderAfterSuccess(FundingOrder fundingOrder, String impUid) {
		//빌더와 다른점은 빌더는 새로운 객체를 생성하지만 set은 기존 객체를 업데이트
		fundingOrder.setImpUid(impUid); // impUid 업데이트
		fundingOrder.setPaymentDate(new Timestamp(System.currentTimeMillis())); // 결제 시간 업데이트
		fundingOrder.setPaymentStatus(FundingOrder.PaymentStatus.PAID); // 결제 상태 업데이트

        //위에는 영속성 상태이지만 가독성을 위해 추가
		fundingOrderRepository.save(fundingOrder);

	}

}

