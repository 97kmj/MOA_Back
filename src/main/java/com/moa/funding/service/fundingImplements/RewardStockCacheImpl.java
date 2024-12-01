package com.moa.funding.service.fundingImplements;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardStockCache;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RewardStockCacheImpl implements RewardStockCache {

	private final Map<Long, List<RewardRequest>> rewardChangeCache = new ConcurrentHashMap<>();

	// 리워드 감소 정보 추가
	@Override
	public void addRewardChanges(Long fundingOrderId, List<RewardRequest> rewardRequests) {
		log.info("리워드 감소 정보 추가: fundingOrderId={}, rewardRequests={}", fundingOrderId, rewardRequests);
		rewardChangeCache.put(fundingOrderId, rewardRequests);

	}

	// 리워드 감소 정보 조회 후 제거
	@Override
	public List<RewardRequest> getAndRemoveRewardChanges(Long fundingOrderId) {
		log.info("리워드 감소 정보 조회 후 제거: fundingOrderId={}", fundingOrderId);
		return rewardChangeCache.remove(fundingOrderId);
	}

}
