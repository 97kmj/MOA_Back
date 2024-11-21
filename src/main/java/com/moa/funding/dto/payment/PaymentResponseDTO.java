package com.moa.funding.dto.payment;

import com.moa.funding.dto.funding.FundingContributionDTO;
import com.moa.funding.dto.funding.FundingOrderDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponseDTO {
	private FundingOrderDTO fundingOrder;
	private FundingContributionDTO fundingContribution;
	private boolean isPaymentVerified;
}