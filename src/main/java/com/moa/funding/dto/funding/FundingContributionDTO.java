package com.moa.funding.dto.funding;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingContributionDTO {
	private Long contributionId;
	private Long fundingOrderId;
	private Long fundingId;
	private Long rewardId;
	private String rewardName; // Reward 이름
	private Long rewardQuantity;
	private BigDecimal rewardPrice;
	private Timestamp contributionDate; // 기부 일자




}