package com.moa.funding.dto.funding;

import java.sql.Timestamp;

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
	private String userName; // 추가: 사용자 이름
	private Timestamp paymentDate; // 추가: 결제 일자
}