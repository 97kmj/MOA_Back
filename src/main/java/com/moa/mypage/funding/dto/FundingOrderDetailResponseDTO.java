package com.moa.mypage.funding.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingOrderDetailResponseDTO {

	private Long fundingOrderId;
	private String userName;
	private Long totalAmount;
	private Timestamp paymentDate;
	private String paymentType;
	private String refundStatus;
	private String address;
	private String phoneNumber;
	private String name;
	private boolean isRefundable;
	private List<FundingContributionResponseDTO> contributions;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class FundingContributionResponseDTO {
		private Long contributionId;
		private Long rewardId;
		private String rewardName;
		private BigDecimal rewardPrice;
		private Long rewardQuantity;
		private Timestamp contributionDate;
	}
}
