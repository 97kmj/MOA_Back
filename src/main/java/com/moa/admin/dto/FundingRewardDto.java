package com.moa.admin.dto;

import java.math.BigDecimal;

import com.moa.entity.Reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingRewardDto {
	private String name; // rewardName;
	private String description; //rewardDescription;
	private BigDecimal price; // rewardPrice;
	private Integer quantity; // stock;
	private Boolean isQuantityLimited; // isLimit;
	private Integer limitPerPerson; // limitQuantity;
	
	public static FundingRewardDto fromReward(Reward reward) {
		return FundingRewardDto.builder()
					.name(reward.getRewardName())
					.description(reward.getRewardDescription())
					.price(reward.getRewardPrice())
					.quantity(reward.getStock())
					.isQuantityLimited(reward.getIsLimit())
					.limitPerPerson(reward.getLimitQuantity())
					.build();
	}
}
