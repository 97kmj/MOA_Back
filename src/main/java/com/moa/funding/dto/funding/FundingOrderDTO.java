package com.moa.funding.dto.funding;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingOrderDTO {
	private Long fundingOrderId;
	private Long totalAmount;
	private String paymentType;
	private String address;
	private String phoneNumber;
	private String name;
	private String userName;
	private Timestamp paymentDate;
}