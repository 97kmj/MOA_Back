package com.moa.funding.service.fundingImplements;

import org.springframework.stereotype.Service;

import com.moa.entity.Reward;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.RewardService;
import com.moa.repository.RewardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
	private final RewardRepository rewardRepository;

	@Override
	public void reduceRewardStock(RewardRequest rewardRequest) {
		// Reward 조회
		Reward reward = getReward(rewardRequest);
		// 재고 검증
		validateRewardStock(reward, rewardRequest.getRewardQuantity());
		// 재고 감소
		reward.setStock(reward.getStock() - rewardRequest.getRewardQuantity().intValue());
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
		if (reward.getStock() < requestedQuantity) {
			throw new RuntimeException("리워드 재고가 부족합니다.");
		}
	}
}
