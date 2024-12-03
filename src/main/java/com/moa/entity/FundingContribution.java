package com.moa.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class FundingContribution {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long contributionId;
	
	@ManyToOne
    @JoinColumn(name = "funding_order_id", nullable = false)
    private FundingOrder fundingOrder;

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @Column(nullable = false)
    private BigDecimal rewardPrice;

    @Column(nullable = false)
    private Long rewardQuantity;

    @Column(nullable = false, updatable = false)
    private Timestamp contributionDate;

}
