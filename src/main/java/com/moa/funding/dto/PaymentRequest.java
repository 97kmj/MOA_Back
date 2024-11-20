package com.moa.funding.dto;

import java.math.BigDecimal;

import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
	private String impUid;
	private Long totalAmount;
	private String paymentType;
	private Long fundingId;
	private Long rewardId;
	private BigDecimal rewardPrice;
	private Long rewardQuantity;
	private String userName;

	public FundingOrder toFundingOrder() {
		return FundingOrder.builder()
			.totalAmount(totalAmount)
			.paymentType(paymentType)
			.build();
	}

	public FundingContribution toFundingContribution() {
		return FundingContribution.builder()
			.rewardPrice(rewardPrice)
			.rewardQuantity(rewardQuantity)
			.build();
	}
}