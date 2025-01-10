package com.moa.funding.service;

import java.util.List;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;

public interface RewardStockCache {
	// 리워드 감소 정보 추가
	void addRewardInfo(String merchantUid, List<RewardRequest> rewardRequests);
// addRewardChanges

	// 리워드 감소 정보 조회 후 제거
	List<RewardRequest> getAndRemoveRewardInfo(String merchantUid);
//getAndRemoveRewardChanges
	// 리워드 선점 제한 관리
	boolean incrementAndCheckLimit(String userName, Long rewardId);

	PaymentRequest createPaymentRequestFromRedis(String merchantUid);

	//incrementAndCheckLimit
	// 리워드 선점 제한 관리
}
