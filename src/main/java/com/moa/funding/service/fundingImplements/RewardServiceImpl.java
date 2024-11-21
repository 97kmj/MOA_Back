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
	public void validateRewardStock(RewardRequest rewardRequest) {
		Reward reward = getReward(rewardRequest);

		if (reward.getStock() < rewardRequest.getRewardQuantity()) {
			throw new RuntimeException("리워드 재고가 부족합니다.");
		}
	}

	@Override
	public void reduceRewardStock(RewardRequest rewardRequest) {
		Reward reward = getReward(rewardRequest);

		if (reward.getStock() < rewardRequest.getRewardQuantity()) {
			throw new RuntimeException("리워드 재고가 부족합니다.");
		}
		reward.setStock(reward.getStock() - rewardRequest.getRewardQuantity().intValue());
		rewardRepository.save(reward);
	}

	@Override
	public Reward getReward(RewardRequest rewardRequest) {
		return rewardRepository.findById(rewardRequest.getRewardId())
			.orElseThrow(() -> new IllegalArgumentException("리워드가 존재하지 않습니다."));
	}

}
