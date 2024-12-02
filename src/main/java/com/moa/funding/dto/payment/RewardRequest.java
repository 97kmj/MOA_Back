package com.moa.funding.dto.payment;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardRequest {
	private Long rewardId;
	private BigDecimal rewardPrice;
	private Long rewardQuantity;
	private Boolean isLimit;            // 구매 제한 여부
	private Integer limitQuantity;      // 구매 제한 수량

}
