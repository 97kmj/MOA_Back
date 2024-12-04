package com.moa.mypage.funding.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingContributionWithFundingDTO {
	private Long fundingId;
	private Long fundingOrderId;
	private String userName;
	private BigDecimal totalAmount;
	private Timestamp paymentDate;
	private String paymentType;
	private String refundStatus;
	private String address;
	private String phoneNumber;
	private String name;
	private Long contributionId;
	private Long rewardId;
	private String rewardName;
	private BigDecimal rewardPrice;
	private Long rewardQuantity;
	private Timestamp contributionDate;

}
