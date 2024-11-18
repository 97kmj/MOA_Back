package com.moa.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class FundingOrder {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundingOrderId;

	@ManyToOne
	@JoinColumn(name="username", nullable = false)
	private User user;
	
	private Long totalAmount;
	private Timestamp paymentDate;
	private String paymentType;
	
	@Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

	private String address;
	private String phoneNumber;
	private String name;
	
	public enum RefundStatus { NOT_REFUNDED, REFUNDED }
	
	
}
