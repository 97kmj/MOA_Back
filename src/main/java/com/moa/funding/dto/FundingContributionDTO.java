package com.moa.funding.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingContributionDTO {
	private Long contributionId;
	private Long fundingOrderId;
	private Long fundingId;
	private Long rewardId;
	private String rewardName;
	private Long rewardQuantity;
	private BigDecimal rewardPrice;
}