package com.moa.funding.dto.payment;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RewardRequest {
	private Long rewardId;
	private BigDecimal rewardPrice;
	private Long rewardQuantity;
}
