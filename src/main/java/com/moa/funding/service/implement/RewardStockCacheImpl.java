package com.moa.funding.service.implement;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import com.moa.entity.FundingOrder;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardStockCache;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;

import lombok.extern.slf4j.Slf4j;

// @Component
// @RequiredArgsConstructor
@Slf4j
public class RewardStockCacheImpl implements RewardStockCache {

	private final RedisTemplate<String, List<RewardRequest>> rewardStockRedisTemplate;
	private final RedisTemplate<String, Integer> userLimitRedisTemplate; // 제한 관리용 RedisTemplate
	private final FundingOrderRepository fundingOrderRepository;

	public RewardStockCacheImpl(RedisTemplate<String, List<RewardRequest>> rewardStockRedisTemplate,
		RedisTemplate<String, Integer> userLimitRedisTemplate , FundingOrderRepository fundingOrderRepository) {
		this.rewardStockRedisTemplate = rewardStockRedisTemplate;
		this.userLimitRedisTemplate = userLimitRedisTemplate;
		this.fundingOrderRepository = fundingOrderRepository;
	}


	private static final String STOCK_PREFIX = "reward_stock:order:";// Redis : 리워드 감소 정보 네임스페이스
	private static final String LIMIT_PREFIX = "reward_limit:user:"; // 리워드 선점 제한 정보 네임스페이스
	private static final int MAX_ATTEMPTS = 3; // 최대 선점 횟수
	private static final Duration LOCK_DURATION = Duration.ofMinutes(10); // 제한 시간


	// 리워드 감소 정보 추가
	@Override
	public void addRewardInfo(String merchantUid, List<RewardRequest> rewardRequests) {
		String key = generateKey(merchantUid);
		try {
			log.info("리워드 감소 정보 추가: fundingOrderId={}, rewardRequests={}", merchantUid, rewardRequests);
			rewardStockRedisTemplate.opsForValue().set(key, rewardRequests, Duration.ofMinutes(100));
		} catch (Exception e) {
			log.error("리워드 감소 정보 추가 실패: fundingOrderId={}, rewardRequests={}", merchantUid, rewardRequests, e);
			throw new RuntimeException("리워드 감소 정보 추가 중 오류 발생", e);
		}

	}

	// 리워드 감소 정보 조회 후 제거
	@Override
	public List<RewardRequest> getAndRemoveRewardInfo(String merchantUid) {
		String key = generateKey(merchantUid);
		try {
			log.info("리워드 감소 정보 조회 후 제거: fundingOrderId={}", merchantUid);
			List<RewardRequest> rewardRequests = rewardStockRedisTemplate.opsForValue().get(key);

			log.info("리워드 정보 조회 getAndRemoveRewardInfo : fundingOrderId={}, rewardRequests={}", merchantUid, rewardRequests);

			if (rewardRequests == null) {
				log.info("리워드 감소 정보 없음: fundingOrderId={}", merchantUid);
				return Collections.emptyList();// null 대신 빈 리스트 반환
			}
			// rewardStockRedisTemplate.delete(key);
			return rewardRequests;

		} catch (Exception e) {
			log.error("리워드 감소 정보 조회 후 제거 실패: fundingOrderId={}", merchantUid, e);
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

	@Override
	public PaymentRequest createPaymentRequestFromRedis(String merchantUid){
		FundingOrder fundingOrder = fundingOrderRepository.findByMerchantUid(merchantUid)
			.orElseThrow(() -> new IllegalArgumentException("주문 정보가 존재하지 않습니다."));

		if (fundingOrder.getPaymentStatus() == FundingOrder.PaymentStatus.PAID) {
			throw new IllegalArgumentException("이미 결제 완료된 주문입니다.");
		}
		List<RewardRequest> rewardRequests = getAndRemoveRewardInfo(merchantUid);

		log.info("리워드 정보 조회 createPaymentRequestFromRedis : fundingOrderId={}, rewardRequests={}", merchantUid, rewardRequests);

		if (rewardRequests == null ||rewardRequests.isEmpty()) {
			throw new IllegalArgumentException("리워드 정보가 존재하지 않습니다.");
		}

		return PaymentRequest.builder()
			.fundingId(fundingOrder.getFunding().getFundingId())
			.totalAmount(fundingOrder.getTotalAmount())
			.merchantUid(merchantUid)
			.rewardList(rewardRequests)
			.build();
	}




	@NotNull
	private String generateLimitKey(String userName, Long rewardId) {
		return LIMIT_PREFIX + userName + ":reward:" + rewardId;
	}


	@NotNull
	private String generateKey(String merchantUid) {
		return STOCK_PREFIX + merchantUid;
	}

}







