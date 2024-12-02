package com.moa.funding.mapper;

import static com.moa.funding.mapper.FundingMapper.*;

import java.math.BigDecimal;
import java.util.List;

import com.moa.entity.Funding;
import com.moa.entity.FundingImage;
import com.moa.entity.Reward;
import com.moa.funding.dto.funding.FundingDetailDTO;

public class FundingDetailMapper {

	private FundingDetailMapper() {
	}

	public static FundingDetailDTO.RewardDTO toRewardDTO(Reward reward) {
		return FundingDetailDTO.RewardDTO.builder()
			.rewardId(reward.getRewardId())
			.rewardName(reward.getRewardName())
			.rewardDescription(reward.getRewardDescription())
			.rewardPrice(reward.getRewardPrice())
			.stock(reward.getStock())
			.isLimit(reward.getIsLimit())
			.limitQuantity(reward.getLimitQuantity())
			.rewardType(reward.getRewardType().toString())
			.build();
	}

	public static FundingDetailDTO.ImageDTO toImageDTO(FundingImage image) {
		return FundingDetailDTO.ImageDTO.builder()
			.imageId(image.getImageId())
			.title(image.getTitle())
			.introduction(image.getIntroduction())
			.imageUrl(image.getImageUrl())
			.build();
	}

	public static FundingDetailDTO toFundingDetailDTO(Funding fundingEntity, List<FundingDetailDTO.RewardDTO> rewards, List<FundingDetailDTO.ImageDTO> images) {
		return FundingDetailDTO.builder()
			.fundingId(fundingEntity.getFundingId())
			.title(fundingEntity.getTitle())
			.fundingMainImageUrl(fundingEntity.getFundingMainImageUrl())
			.fundingUserName(fundingEntity.getUser().getUsername())
			.totalAmount(fundingEntity.getCurrentAmount() != null ? (fundingEntity.getCurrentAmount()) : BigDecimal.ZERO)
			.goalAmount(fundingEntity.getGoalAmount())
			.startDate(fundingEntity.getStartDate())
			.endDate(fundingEntity.getEndDate())
			.introduction(fundingEntity.getIntroduction())
			.rewards(rewards)
			.images(images)
			.achievementRate(calculateAchievementRate(fundingEntity))
			.remainingDays(calculateRemainingDays(fundingEntity.getEndDate()))
			.build();
	}


}
