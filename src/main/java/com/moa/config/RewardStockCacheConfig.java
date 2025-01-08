package com.moa.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardStockCache;
import com.moa.funding.service.implement.InMemoryRewardStockCacheImpl;
import com.moa.funding.service.implement.RewardStockCacheImpl;

@Configuration
public class RewardStockCacheConfig {

	@Bean
	@Primary
	public RewardStockCache rewardStockCache(RedisTemplate<String, List<RewardRequest>> rewardStockRedisTemplate,
		RedisTemplate<String, Integer> userLimitRedisTemplate) {
		return new RewardStockCacheImpl(rewardStockRedisTemplate, userLimitRedisTemplate);
	}


	@Bean
	public RewardStockCache inMemoryRewardStockCache() {
		return new InMemoryRewardStockCacheImpl();
	}


}
