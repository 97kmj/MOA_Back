package com.moa.funding.dto.payment;

import java.util.Date;
import java.util.List;

import com.moa.funding.dto.funding.FundingContributionDTO;
import com.moa.funding.dto.funding.FundingOrderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
	private FundingOrderDTO fundingOrder;
	private List<FundingContributionDTO> fundingContribution;
	private boolean isPaymentVerified;
}