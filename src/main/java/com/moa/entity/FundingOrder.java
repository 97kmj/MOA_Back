package com.moa.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
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

	@ManyToOne
	@JoinColumn(name = "funding_id", nullable = false)
	private Funding funding;

	private String address;
	private String phoneNumber;
	private String name;

	@Column(nullable = true, unique = true) // 결제 성공 시 설정
	private String impUid;

	@Column(nullable = false, unique = true) // 사전 등록 시 설정
	private String merchantUid;


	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus; // 결제 상태 (PENDING, PAI

	
	public enum RefundStatus { NOT_REFUNDED, REFUNDED }

	public enum PaymentStatus { PENDING, PAID, FAILED, CANCELED }
	
}
