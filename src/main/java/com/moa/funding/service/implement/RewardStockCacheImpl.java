package com.moa.funding.service.implement;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardStockCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RewardStockCacheImpl implements RewardStockCache {

	private final RedisTemplate<String, List<RewardRequest>> rewardStockRedisTemplate;
	private final RedisTemplate<String, Integer> userLimitRedisTemplate; // 제한 관리용 RedisTemplate


	private static final String STOCK_PREFIX = "reward_stock:order:";// Redis : 리워드 감소 정보 네임스페이스

	private static final String LIMIT_PREFIX = "reward_limit:user:"; // 리워드 선점 제한 정보 네임스페이스
	private static final int MAX_ATTEMPTS = 3; // 최대 선점 횟수
	private static final Duration LOCK_DURATION = Duration.ofMinutes(10); // 제한 시간



	// 리워드 감소 정보 추가
	@Override
	public void addRewardChanges(Long fundingOrderId, List<RewardRequest> rewardRequests) {
		String key = generateKey(fundingOrderId);
		try {
			log.info("리워드 감소 정보 추가: fundingOrderId={}, rewardRequests={}", fundingOrderId, rewardRequests);
			rewardStockRedisTemplate.opsForValue().set(key, rewardRequests, Duration.ofMinutes(10));
		} catch (Exception e) {
			log.error("리워드 감소 정보 추가 실패: fundingOrderId={}, rewardRequests={}", fundingOrderId, rewardRequests, e);
			throw new RuntimeException("리워드 감소 정보 추가 중 오류 발생", e);
		}

	}

	// 리워드 감소 정보 조회 후 제거
	@Override
	public List<RewardRequest> getAndRemoveRewardChanges(Long fundingOrderId) {
		String key = generateKey(fundingOrderId);
		try {
			log.info("리워드 감소 정보 조회 후 제거: fundingOrderId={}", fundingOrderId);
			List<RewardRequest> rewardRequests = rewardStockRedisTemplate.opsForValue().get(key);

			if (rewardRequests == null) {
				log.info("리워드 감소 정보 없음: fundingOrderId={}", fundingOrderId);
				return Collections.emptyList();// null 대신 빈 리스트 반환
			}
			rewardStockRedisTemplate.delete(key);
			return rewardRequests;

		} catch (Exception e) {
			log.error("리워드 감소 정보 조회 후 제거 실패: fundingOrderId={}", fundingOrderId, e);
			throw new RuntimeException("리워드 감소 정보 조회 후 제거 중 오류 발생", e);
		}
	}

	// 리워드 선점 제한 관리
	@Override
	public boolean incrementAndCheckLimit(String userName, Long rewardId) {
		String limitKey = generateLimitKey(userName, rewardId);
		try {
			log.info("리워드 선점 제한 확인: userName={}, rewardId={}", userName, rewardId);

			// 현재 선점 횟수 조회
			Integer currentCount = userLimitRedisTemplate.opsForValue().get(limitKey);

			// 선점 횟수가 제한 초과 여부 확인
			if (currentCount != null && currentCount >= MAX_ATTEMPTS) {
				log.warn("리워드 선점 횟수 초과: userName={}, rewardId={}, count={}", userName, rewardId, currentCount);
				return false; // 제한 초과
			}
			// 선점 횟수 증가
			userLimitRedisTemplate.opsForValue().increment(limitKey);

			// TTL 설정
			if (currentCount == null) {
				userLimitRedisTemplate.expire(limitKey, LOCK_DURATION);
			}

			log.info("리워드 선점 횟수 업데이트: userName={}, rewardId={}, count={}", userName, rewardId, currentCount == null ? 1 : currentCount + 1);
			return true;

		} catch (Exception e) {
			log.error("리워드 선점 제한 처리 중 오류 발생: userName={}, rewardId={}", userName, rewardId, e);
			throw new RuntimeException("리워드 선점 제한 처리 중 오류 발생", e);
		}
	}


	@NotNull
	private String generateLimitKey(String userName, Long rewardId) {
		return LIMIT_PREFIX + userName + ":reward:" + rewardId;
	}


	@NotNull
	private String generateKey(Long fundingOrderId) {
		return STOCK_PREFIX + fundingOrderId;
	}

}







// private final Map<Long, List<RewardRequest>> rewardChangeCache = new ConcurrentHashMap<>();
//
// // 리워드 감소 정보 추가
// @Override
// public void addRewardChanges(Long fundingOrderId, List<RewardRequest> rewardRequests) {
// 	log.info("리워드 감소 정보 추가: fundingOrderId={}, rewardRequests={}", fundingOrderId, rewardRequests);
// 	rewardChangeCache.put(fundingOrderId, rewardRequests);
//
// }
//
// // 리워드 감소 정보 조회 후 제거
// @Override
// public List<RewardRequest> getAndRemoveRewardChanges(Long fundingOrderId) {
// 	log.info("리워드 감소 정보 조회 후 제거: fundingOrderId={}", fundingOrderId);
// 	return rewardChangeCache.remove(fundingOrderId);
// }
