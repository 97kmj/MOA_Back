package com.moa.funding.service.fundingImplements;

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

	@Override
	@Transactional
	public void refundIndividualFunding(Long fundingOrderId) {
		FundingOrder fundingOrder = fundingManagementRepositoryCustom.findRefundableOrderById(fundingOrderId)
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 주문 ID입니다."));

		if (fundingOrder.getRefundStatus() == FundingOrder.RefundStatus.REFUNDED) {
			throw new IllegalStateException("환불이 불가능한 주문입니다.");
		}

		portOneService.refundOrder(fundingOrder);

		restoreCustomRewardStock(fundingOrderId);

		updateFundingCurrentAmount(fundingOrder);

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
				reward.setStock(reward.getStock() + contribution.getRewardQuantity().intValue());
				rewardRepository.save(reward);
			}

		}
	}

	private void updateFundingCurrentAmount(FundingOrder fundingOrder) {
		Funding funding = fundingOrder.getFunding();
		BigDecimal refundAmount = BigDecimal.valueOf(fundingOrder.getTotalAmount());
		BigDecimal newAmount = funding.getCurrentAmount().subtract(refundAmount);

		if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalStateException("펀딩의 총 금액이 음수가 될 수 없습니다.");
		}

		// 영속성 컨텍스트에서 관리되는 엔티티이므로 save 호출하지 않아도 업데이트된다.
		funding.setCurrentAmount(newAmount);
	}

	@Override
	// @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
	// @Scheduled(cron = "0 0/1 * * * *") // 매 1분마다 실행
	@Scheduled(cron = "0 0 * * * *") // 매 시간 0분에 실행
	@Transactional
	public void scheduleUpdateToFailedAndRefund() {
		log.info("펀딩 실패 자동 환불 스케줄링 시작");

		//실패한 펀딩 ID 가져오기
		List<Long> failedFundingIds = fundingManagementRepositoryCustom.updateFundingToFailedAndGetIds(Instant.now());

		if (failedFundingIds.isEmpty()) {
			log.info("환불 대상 펀딩이 없습니다.");
			return;
		}

		for (Long fundingId : failedFundingIds) {
			try {
				refundFailedFunding(fundingId);
				log.info("펀딩 ID={} 환불 성공", fundingId);
			} catch (Exception e) {
				log.error("펀딩 ID={} 환불 실패: {}", fundingId, e.getMessage());
			}
		}
		log.info("자동 환불 스케줄링 종료");
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

}


