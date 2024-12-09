package com.moa.shop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CartOrderRequest {
	private Long totalAmount;
	private String paymentType;
	private List<CartDto> cartList;
	private String address;
	private String phoneNumber;
	private String name;
}
