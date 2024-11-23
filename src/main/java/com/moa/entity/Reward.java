package com.moa.entity;

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
	private String rewardName;
	
	@Lob
    private String rewardDescription;
	@Column(nullable=false)
	private Long rewardPrice;
	
	private Integer stock;
	private Boolean isLimit;
	private Integer limitQuantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RewardType rewardType;

	public enum RewardType {
		BASIC,  // 기본 리워드 (리워드 없는 후원)
		CUSTOM  // 커스텀 리워드
	}

}
