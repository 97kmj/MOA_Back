package com.moa.funding.service;

import java.time.Instant;
import java.util.List;


public interface FundingRefundService {

	void refundIndividualFunding(Long fundingOrderId); // 단순 변심 주문의 환불 처리

	// List<Long> updateFundingToFailedAndGetIds(Instant now);
	void scheduleUpdateToFailedAndRefund(); // 펀딩 실패 자동 환불 스케줄링
	void refundFailedFunding(Long fundingId); // 특정 펀딩의 환불 처리
}
