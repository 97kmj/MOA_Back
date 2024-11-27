package com.moa.funding.service.fundingImplements;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.mapper.FundingPaymentMapper;
import com.moa.funding.service.FundingPaymentService;
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

	@Override
	@Transactional
	public void processFundingContribution(String impUid, PaymentRequest paymentRequest) {
		log.debug("결제된 펀딩 후원 처리   - impUid: {}, PaymentRequest: {}", impUid, paymentRequest);
		// Step 1: 검증 및 중복 결제 리워드 정보 확인
		validatePayment(impUid, paymentRequest);

		// Step 2: 사용자 조회
		User user = getUser(paymentRequest);

		// Step 3: 펀딩 주문 생성 및 저장
		FundingOrder savedOrder = createAndSaveFundingOrder(paymentRequest, user);

		// Step 4: 펀딩 조회 및 후원 생성 및 저장
		createAndSaveFundingContribution(paymentRequest, savedOrder);

		// Step 5: 펀딩의 currentAmount 업데이트
		updateFundingCurrentAmount(paymentRequest);
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

	private FundingOrder createAndSaveFundingOrder(PaymentRequest paymentRequest, User user) {
		FundingOrder order = FundingPaymentMapper.toFundingOrder(paymentRequest, user);
		return fundingOrderRepository.save(order);
	}


	private void createAndSaveFundingContribution(PaymentRequest paymentRequest,FundingOrder savedOrder) {
		//Step1: 펀딩 조회
		Funding funding = getFunding(paymentRequest);

		//Step2: 각 리워드별 후원 생성 및 저장
		for (RewardRequest rewardRequest : paymentRequest.getRewardList()) {
			// Step 3: 리워드 조회
			Reward reward = rewardService.getReward(rewardRequest);
			// Step 3.1: 재고 감소
			rewardService.reduceRewardStock(rewardRequest);
			// Step 3.2: 후원 생성 및 저장
			FundingContribution contribution = FundingPaymentMapper.toFundingContribution(rewardRequest, savedOrder, funding,
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

}

