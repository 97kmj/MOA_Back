package com.moa.funding.service.fundingImplements;

import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.FundingOrder;
import com.moa.funding.repository.FundingStatusRepositoryCustom;
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
	private final FundingStatusRepositoryCustom fundingStatusRepository;
	private final PortOneService portOneService;
	private final RewardRepository rewardRepository;

	@Override
	// @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
	// @Scheduled(cron = "0 0/1 * * * *") // 매 1분마다 실행
	@Scheduled(cron = "0 0 * * * *") // 매 시간 0분에 실행
	@Transactional
	public void scheduleUpdateToFailedAndRefund() {
	  log.info("펀딩 실패 자동 환불 스케줄링 시작");

	  //실패한 펀딩 ID 가져오기
		List<Long> failedFundingIds = fundingStatusRepository.updateFundingToFailedAndGetIds(Instant.now());

		if (failedFundingIds.isEmpty()) {
			log.info("환불 대상 펀딩이 없습니다.");
			return;
		}

		for(Long fundingId : failedFundingIds) {
		try {
			refundFailedFunding(fundingId);
			log.info("펀딩 ID={} 환불 성공", fundingId);
		}catch (Exception e) {
			log.error("펀딩 ID={} 환불 실패: {}", fundingId, e.getMessage());
			}
		}
		log.info("자동 환불 스케줄링 종료");
	}

	@Override
	public void refundFailedFunding(Long fundingId) {
		log.info("펀딩 ID={} 환불 시작", fundingId);

		//환불 대상 주문 조회
		List<FundingOrder> orders = fundingStatusRepository.findOrdersByFundingId(fundingId);

		for(FundingOrder order : orders) {
		try {
			//포트원 환불 요청
			portOneService.refundOrder(order);
			//환불 상태 업데이트
			order.setRefundStatus(FundingOrder.RefundStatus.REFUNDED);
			fundingOrderRepository.save(order);

			log.info("환불 성공: orderId={}, amount={}", order.getFundingOrderId(), order.getTotalAmount());

		}catch (Exception e) {
			log.error("환불 실패: orderId={}, error={}", order.getFundingOrderId(), e.getMessage());
			}
		}

	}

}


