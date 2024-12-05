package com.moa.shop.service;

import java.util.List;

import com.moa.shop.dto.OrderItemDto;
import com.moa.shop.dto.OrderPaymentRequest;

public interface OrderPaymentService {
	void processPayment(OrderPaymentRequest orderPaymentRequest, String username, List<OrderItemDto> saleData ) throws Exception;
}
