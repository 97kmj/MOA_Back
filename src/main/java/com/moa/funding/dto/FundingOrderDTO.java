package com.moa.funding.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingOrderDTO {
	private Long fundingOrderId;
	private Long totalAmount;
	private String paymentType;
	private String address;
	private String phoneNumber;
	private String name;
}