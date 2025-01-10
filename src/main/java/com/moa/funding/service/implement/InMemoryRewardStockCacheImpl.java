package com.moa.funding.service.implement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardStockCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryRewardStockCacheImpl implements RewardStockCache {

	private final Map<String, List<RewardRequest>> rewardChangeCache = new ConcurrentHashMap<>();

	@Override
	public void addRewardInfo(String merchantUid, List<RewardRequest> rewardRequests) {
		log.info("리워드 감소 정보 추가: fundingOrderId={}, rewardRequests={}", merchantUid, rewardRequests);
		rewardChangeCache.put(merchantUid, rewardRequests);

	}

	// 리워드 감소 정보 조회 후 제거
	@Override
	public List<RewardRequest> getAndRemoveRewardInfo(String merchantUid) {
		log.info("리워드 감소 정보 조회 후 제거: fundingOrderId={}", merchantUid);
		return rewardChangeCache.remove(merchantUid);

	}

	@Override
	public boolean incrementAndCheckLimit(String userName, Long rewardId) {
		return false;
	}

	@Override
	public PaymentRequest createPaymentRequestFromRedis(String merchantUid) {
		return null;
	}
}



