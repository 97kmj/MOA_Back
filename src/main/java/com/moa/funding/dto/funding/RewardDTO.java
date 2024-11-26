package com.moa.funding.dto.funding;

import java.math.BigDecimal;
import java.util.Date;

import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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