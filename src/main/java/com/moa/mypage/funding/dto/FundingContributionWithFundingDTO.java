package com.moa.mypage.funding.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.moa.funding.dto.funding.FundingDetailDTO;
import com.moa.funding.dto.funding.RewardDTO;
import com.moa.funding.dto.payment.RewardRequest;

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
	private String orderUserName;
	private Long totalAmount;
	private Timestamp paymentDate;
	private String paymentType;
	private String refundStatus;
	private String address;
	private String phoneNumber;
	private String name;
	private List<RewardDTO> rewards;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class RewardDTO {
		private Long rewardId;
		private String rewardName;
		private BigDecimal rewardPrice;
		private Long rewardQuantity;
		private Long contributionId;
		private Timestamp contributionDate;
	}

}
