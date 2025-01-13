package com.moa.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.repository.FundingSelectRepositoryCustom;
import com.moa.funding.service.RewardStockCache;
import com.moa.funding.service.implement.InMemoryRewardStockCacheImpl;
import com.moa.funding.service.implement.RewardStockCacheImpl;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;

@Configuration
public class RewardStockCacheConfig {

	@Bean
	@Primary
	public RewardStockCache rewardStockCache(RedisTemplate<String, List<RewardRequest>> rewardStockRedisTemplate,
		RedisTemplate<String, Integer> userLimitRedisTemplate, FundingOrderRepository fundingOrderRepository,
		FundingSelectRepositoryCustom fundingSelectRepositoryCustom) {
		return new RewardStockCacheImpl(rewardStockRedisTemplate, userLimitRedisTemplate , fundingOrderRepository, fundingSelectRepositoryCustom);
	}


	@Bean
	public RewardStockCache inMemoryRewardStockCache() {
		return new InMemoryRewardStockCacheImpl();
	}


}
