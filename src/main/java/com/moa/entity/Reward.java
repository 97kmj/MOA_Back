package com.moa.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Reward {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rewardId;
	
	@ManyToOne
	@JoinColumn(name="funding_id",nullable=false)
	private Funding funding;
	
	@Column(nullable=false)
	private String rewardName; //name
	
	@Lob
    private String rewardDescription; //description
	@Column(nullable=false)
	private BigDecimal rewardPrice; //price
	
	private Integer stock; //quantity
	private Boolean isLimit; //isQuantityLimited
	private Integer limitQuantity; //limitPerPerson

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RewardType rewardType; //rewardType

	public enum RewardType {
		BASIC,  // 기본 리워드 (리워드 없는 후원)
		CUSTOM  // 커스텀 리워드
	}

}
