package com.moa.funding.dto.payment;

import java.math.BigDecimal;
import java.util.List;

import com.moa.entity.User;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PaymentRequest {
	private String impUid;
	private Long totalAmount;
	private String paymentType;
	private Long fundingId;
	private Long rewardId;
	private List<RewardRequest> rewardList;
	private BigDecimal rewardPrice;
	private Long rewardQuantity;
	// private String userName;
	private String userName;
	private String address;
	private String phoneNumber;
	private String name; // 배송지 정보
	private String merchantUid;
	private BigDecimal amount;




}