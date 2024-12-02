package com.moa.funding.service;

import java.util.List;

import com.moa.funding.dto.payment.RewardRequest;

public interface RewardStockCache {
	// 리워드 감소 정보 추가
	void addRewardChanges(Long fundingOrderId, List<RewardRequest> rewardRequests);

	// 리워드 감소 정보 조회 후 제거
	List<RewardRequest> getAndRemoveRewardChanges(Long fundingOrderId);
}
