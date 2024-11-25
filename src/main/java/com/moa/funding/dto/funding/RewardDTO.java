package com.moa.funding.dto.funding;

import java.math.BigDecimal;

import com.moa.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RewardDTO {
	private String name; // rewardName;
	private String description; //rewardDescription;
	private BigDecimal price; // rewardPrice;
	private Integer quantity; // stock;
	private Boolean isQuantityLimited; // isLimit;
	private Integer limitPerPerson; // limitQuantity;
	private String rewardType; // rewardType; /

	// Getter/Setter or Lombok @Data
}