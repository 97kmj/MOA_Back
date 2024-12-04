package com.moa.shop.service;

import com.moa.entity.OrderItem;
import com.moa.shop.dto.OrderPaymentRequest;

public interface OrderPaymentService {
	void processPayment(OrderPaymentRequest orderPaymentRequest, String username, OrderItem saleData ) throws Exception;
}
