package com.moa.funding.service;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.PaymentResponseDTO;
import com.moa.funding.dto.payment.RewardRequest;

public interface FundingPaymentService {
	PaymentResponseDTO processPayment(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(PaymentRequest paymentRequest);


}

