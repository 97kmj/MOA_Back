package com.moa.shop.service;

import com.moa.shop.dto.OrderPaymentRequest;

public interface OrderPaymentService {
	void processPayment(OrderPaymentRequest orderPaymentRequest) throws Exception;
}
