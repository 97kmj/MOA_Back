package com.moa.funding.service.implement;

import org.springframework.stereotype.Service;

import com.moa.entity.Reward;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.exception.RewardStockException;
import com.moa.funding.service.RewardService;
import com.moa.repository.RewardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardServiceImpl implements RewardService {
	private final RewardRepository rewardRepository;

	@Override
	public void reduceRewardStock(RewardRequest rewardRequest) {
		// Reward 조회
		// Reward reward = getReward(rewardRequest);

		Reward reward = rewardRepository.findByIdWithLock(rewardRequest.getRewardId())
			.orElseThrow(() -> new IllegalArgumentException("리워드가 존재하지 않습니다."));

		// BASIC 리워드 처리 여부 확인
		if (isBasicReward(reward)) {
			return; // BASIC 리워드는 작업 건너뜀
		}
		// 재고가 null인 경우 "수량 제한 없음"으로 간주
		if (stockIsLimitless(reward)) {
			log.info("리워드 '{}'는 수량 제한이 없습니다. (Stock is null)", reward.getRewardName());
			return; // 수량 제한이 없으므로 재고를 감소할 필요 없음
		}

		// 재고 검증
		validateRewardStock(reward, rewardRequest.getRewardQuantity());
		// 재고 감소
		reward.setStock(reward.getStock() - rewardRequest.getRewardQuantity().intValue());
		rewardRepository.save(reward);
	}

	@Override
	public void restoreRewardStock(RewardRequest rewardRequest) {
		Reward reward = getReward(rewardRequest);

		// BASIC 리워드 처리 여부 확인
		if (isBasicReward(reward)) {
			return; // BASIC 리워드는 작업 건너뜀
		}
		//재고 복구
		if (stockIsLimitless(reward)) {
			log.info("리워드 '{}'는 수량 제한이 없습니다. ", reward.getRewardName());
			return; // 수량 제한이 없으므로 재고를 복구할 필요 없음
		}

		reward.setStock(reward.getStock() + rewardRequest.getRewardQuantity().intValue());
		log.info("리워드 '{}'의 재고 복구: {}", reward.getRewardName(), rewardRequest.getRewardQuantity());

		rewardRepository.save(reward);
	}

	@Override
	public Reward getReward(RewardRequest rewardRequest) {
		return rewardRepository.findById(rewardRequest.getRewardId())
			.orElseThrow(() -> new IllegalArgumentException("리워드가 존재하지 않습니다."));
	}

	@Override
	public void validateRewardStock(RewardRequest rewardRequest) {
		// Reward 조회
		Reward reward = getReward(rewardRequest);
		// 재고 검증
		validateRewardStock(reward, rewardRequest.getRewardQuantity());
	}

	private void validateRewardStock(Reward reward, Long requestedQuantity) {

		if (reward.getRewardType() == Reward.RewardType.BASIC) {
			log.info("기본 리워드(BASIC)는 재고 검사를 건너뜀 - RewardId: {}", reward.getRewardId());
			return;
		}

		if (reward.getStock() < requestedQuantity) {
			throw new RewardStockException("리워드 재고가 부족합니다. - 리워드명: " + reward.getRewardName());
		}
	}

	private boolean isBasicReward(Reward reward) {
		if (reward.getRewardType() == Reward.RewardType.BASIC) {
			log.info("기본 리워드(BASIC)는 작업을 건너뜀 - RewardId: {}", reward.getRewardId());
			return true;
		}
		return false;
	}

	private boolean stockIsLimitless(Reward reward) {
		return reward.getStock() == null;
	}

}
