package com.moa.funding.service.implement;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.funding.repository.FundingManagementRepositoryCustom;
import com.moa.funding.service.FundingRefundService;
import com.moa.funding.service.portone.PortOneService;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundingRefundServiceImpl implements FundingRefundService {

	private final FundingOrderRepository fundingOrderRepository;
	private final FundingManagementRepositoryCustom fundingManagementRepositoryCustom; //펀딩 성공 실패는 여기 있음
	private final PortOneService portOneService;
	private final RewardRepository rewardRepository;
	private final FundingRepository fundingRepository;

	// @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
	// @Scheduled(cron = "0 0 * * * *") // 매 시간 0분에 실행
	@Override
	@Scheduled(cron = "0 0/5 * * * *") // 매 1분마다 실행
	@Transactional
	public void scheduleUpdateToFailedAndRefund() {
		log.debug("펀딩 실패 자동 환불 스케줄링 시작");

		//실패한 펀딩 ID 가져오기
		List<Long> failedFundingIds = fundingManagementRepositoryCustom.updateFundingToFailedAndGetIds(Instant.now());

		if (failedFundingIds.isEmpty()) {
			log.debug("환불 대상 펀딩이 없습니다.");
			return;
		}

		for (Long fundingId : failedFundingIds) {
			try {
				refundFailedFunding(fundingId);
				log.debug("펀딩 ID={} 환불 성공", fundingId);
			} catch (Exception e) {
				log.error("펀딩 ID={} 환불 실패: {}", fundingId, e.getMessage());
			}
		}
		log.info("자동 환불 스케줄링 종료");
	}

	@Override
	@Transactional
	public void refundIndividualFunding(Long fundingOrderId, String userName) {
		FundingOrder fundingOrder = fundingManagementRepositoryCustom.findRefundableOrderById(fundingOrderId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID입니다."));

		validateRefundRequest(userName, fundingOrder);

		portOneService.refundOrder(fundingOrder);

		restoreCustomRewardStock(fundingOrderId);

		updateFundingCurrentAmount(fundingOrder);

		fundingManagementRepositoryCustom.validateAndUpdateFundingStatuses();

		fundingOrder.setRefundStatus(FundingOrder.RefundStatus.REFUNDED);
		fundingManagementRepositoryCustom.updateRefundStatus(fundingOrder);

		log.info("환불 완료: fundingOrderId={}, 환불 금액={}, 펀딩 ID={}",
			fundingOrderId, fundingOrder.getTotalAmount(), fundingOrder.getFunding().getFundingId());

	}

	private void restoreCustomRewardStock(Long fundingOrderId) {
		//Step 1. OrderID로 찾은 contributions 가져온다.
		List<FundingContribution> contributions = fundingManagementRepositoryCustom.findContributionsByFundingOrderId(
			fundingOrderId);

		for (FundingContribution contribution : contributions) {
			// Step 2. contribution의 reward를 가져온다.
			Reward reward = contribution.getReward();

			// Step 3. reward가 custom이면 stock을 원래대로 복구한다.
			if (reward != null && reward.getRewardType() == Reward.RewardType.CUSTOM) {
				if (stockIsLimitless(reward)) {
					log.info("Reward ID {}는 수량 무제한이므로 재고 복구를 건너뜁니다.", reward.getRewardId());
					continue;
				}

				reward.setStock(reward.getStock() + contribution.getRewardQuantity().intValue());
				rewardRepository.save(reward);
			}

		}
	}

	private void updateFundingCurrentAmount(FundingOrder fundingOrder) {
		Funding funding = fundingOrder.getFunding();
		BigDecimal refundAmount = BigDecimal.valueOf(fundingOrder.getTotalAmount());
		BigDecimal newAmount = funding.getCurrentAmount().subtract(refundAmount);

		log.info("펀딩 환불 신청들어왔을 당시의  현재 금액  CurrentAmount: {}", funding.getCurrentAmount());
		log.info("펀딩 환불의 금액 RefundAmount: {}", refundAmount);
		if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalStateException("펀딩의 총 금액이 음수가 될 수 없습니다.");
		}

		// 영속성 컨텍스트에서 관리되는 엔티티이므로 save 호출하지 않아도 업데이트된다.
		funding.setCurrentAmount(newAmount);

		// 영속성 컨텍스트에서 관리되는 엔티티이므로 save 호출하지 않아도 업데이트 되지만 가독성 위해 추가
		fundingRepository.save(funding);

		log.info("펀딩 환불 후의 현재 금액 CurrentAmount: {}", funding.getCurrentAmount());
	}

	@Override
	public void refundFailedFunding(Long fundingId) {
		log.info("펀딩 ID={} 환불 시작", fundingId);

		//환불 대상 주문 조회
		List<FundingOrder> orders = fundingManagementRepositoryCustom.findOrdersByFundingId(fundingId);

		for (FundingOrder order : orders) {
			try {
				//포트원 환불 요청
				portOneService.refundOrder(order);
				//환불 상태 업데이트
				order.setRefundStatus(FundingOrder.RefundStatus.REFUNDED);
				fundingOrderRepository.save(order);

				log.info("환불 성공: orderId={}, amount={}", order.getFundingOrderId(), order.getTotalAmount());

			} catch (Exception e) {
				log.error("환불 실패: orderId={}, error={}", order.getFundingOrderId(), e.getMessage());
			}
		}

	}

	private void validateRefundRequest(String userName, FundingOrder fundingOrder) {
		// 유저 권한 검증
		if (!fundingOrder.getUser().getUsername().equals(userName)) {
			throw new IllegalStateException("해당 주문에 대한 권한이 없습니다.");
		}

		if (fundingOrder.getRefundStatus() == FundingOrder.RefundStatus.REFUNDED) {
			throw new IllegalStateException("환불이 불가능한 주문입니다.");
		}

		if(FundingOrder.PaymentStatus.PENDING == fundingOrder.getPaymentStatus()) {
			throw new IllegalStateException("결제가 완료되지 않은 주문은 환불할 수 없습니다.");
		}
	}

	private boolean stockIsLimitless(Reward reward) {
		return reward.getStock() == null;
	}

}


