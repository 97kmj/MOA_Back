package com.moa.funding.dto.funding;

import java.math.BigDecimal;
import java.time.Instant;

import com.moa.entity.Funding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FundingListDTO {
	private Long fundingId;
	private String title;
	private String fundingMainImageUrl;
	private String fundingUserName;
	private BigDecimal goalAmount;
	private BigDecimal totalAmount;
	private Instant startDate;
	private Instant endDate;
	private String fundingStatus;
	private int achievementRate; // 달성률 (0~100)
	private int remainingDays; // 남은 날짜
}