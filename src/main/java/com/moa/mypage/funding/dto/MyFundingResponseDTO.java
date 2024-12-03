package com.moa.mypage.funding.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.moa.entity.FundingOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyFundingResponseDTO {
	private Long fundingOrderId;
	private Long fundingId;
	private String fundingTitle;
	private String fundingImage;
	private String fundingStatus;
	private BigDecimal goalAmount;
	private Instant startDate;
	private Instant endDate;




}
