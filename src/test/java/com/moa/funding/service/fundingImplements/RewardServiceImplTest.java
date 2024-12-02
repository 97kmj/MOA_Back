package com.moa.funding.service.fundingImplements;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.moa.entity.Reward;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.exception.RewardStockException;
import com.moa.repository.RewardRepository;

class RewardServiceTest {

	@Mock
	private RewardRepository rewardRepository;

	@InjectMocks
	private RewardServiceImpl rewardService;

	public RewardServiceTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("리워드 재고 감소 테스트")
	void testReduceRewardStock() {
		// Given
		Reward reward = Reward.builder()
			.rewardId(1L)
			.stock(10)
			.build();
		RewardRequest rewardRequest = RewardRequest.builder()
			.rewardId(1L)
			.rewardQuantity(3L)
			.build();

		when(rewardRepository.findById(1L)).thenReturn(java.util.Optional.of(reward));

		// When
		rewardService.reduceRewardStock(rewardRequest);

		// Then
		assertEquals(7, reward.getStock(), "재고는 7이어야 합니다.");
		verify(rewardRepository).save(reward);
	}
	@Test
	@DisplayName("리워드 재고 부족 시 예외 발생 테스트")
	void testReduceRewardStockFailure() {
		// Given
		Reward reward = Reward.builder()
			.rewardId(1L)
			.rewardName("테스트 리워드") // 리워드명 설정
			.stock(2)
			.build();

		RewardRequest rewardRequest = RewardRequest.builder()
			.rewardId(1L)
			.rewardQuantity(3L)
			.build();

		when(rewardRepository.findById(1L)).thenReturn(java.util.Optional.of(reward));

		// When & Then
		RewardStockException exception = assertThrows(RewardStockException.class, () ->
			rewardService.reduceRewardStock(rewardRequest)
		);

		// 예외 메시지 검증
		assertEquals("리워드 재고가 부족합니다. - 리워드명: 테스트 리워드", exception.getMessage());
		verify(rewardRepository, never()).save(any());
	}
	@Test
	@DisplayName("리워드 조회 성공 테스트")
	void testGetReward() {
		// Given
		Reward reward = Reward.builder()
			.rewardId(1L)
			.stock(10)
			.build();
		RewardRequest rewardRequest = RewardRequest.builder()
			.rewardId(1L)
			.build();

		when(rewardRepository.findById(1L)).thenReturn(java.util.Optional.of(reward));

		// When
		Reward result = rewardService.getReward(rewardRequest);

		// Then
		assertEquals(reward, result, "조회된 리워드가 동일해야 합니다.");
	}

	@Test
	@DisplayName("리워드 조회 실패 테스트")
	void testGetRewardFailure() {
		// Given
		RewardRequest rewardRequest = RewardRequest.builder()
			.rewardId(1L)
			.build();

		when(rewardRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		// When & Then
		RuntimeException exception = assertThrows(IllegalArgumentException.class, () ->
			rewardService.getReward(rewardRequest)
		);

		assertEquals("리워드가 존재하지 않습니다.", exception.getMessage());
	}
}
