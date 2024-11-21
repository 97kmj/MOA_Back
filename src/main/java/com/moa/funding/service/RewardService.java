package com.moa.funding.service;

import com.moa.entity.Reward;
import com.moa.funding.dto.payment.RewardRequest;

public interface RewardService {
	void validateRewardStock(RewardRequest rewardRequest);
	void reduceRewardStock(RewardRequest rewardRequest);
	Reward getReward(RewardRequest rewardRequest);
}
