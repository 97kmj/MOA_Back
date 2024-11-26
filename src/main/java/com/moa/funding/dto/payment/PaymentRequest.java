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
	private String userName; // 사용자 정보
	private String address; // 배송지 정보
	private String phoneNumber; // 배송지 정보
	private String name; // 배송지 정보
	private String merchantUid;
	private BigDecimal amount;




}