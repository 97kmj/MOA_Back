package com.moa.shop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
	   private OrderPaymentRequest requestData;
	   private String username;
	   private List<OrderItemDto> saleDatas;
}
