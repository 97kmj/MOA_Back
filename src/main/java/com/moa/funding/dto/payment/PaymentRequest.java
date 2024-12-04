package com.moa.funding.dto.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
	private String impUid;
	private Long totalAmount;
	private String paymentType;
	private Long fundingId;
	private Long rewardId;
	private List<RewardRequest> rewardList;
	@Setter
	private String userName; // 사용자 정보
	private String address; // 배송지 정보
	private String phoneNumber; // 배송지 정보
	private String name; // 배송지 정보
	private String merchantUid;
	private BigDecimal amount;

}