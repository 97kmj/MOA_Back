package com.moa.funding.service;

import com.moa.funding.dto.FundingOrderDTO;
import com.moa.funding.dto.PaymentRequest;

public interface FundingPaymentService {
	FundingOrderDTO processPayment(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(Long fundingId, Long amount);
}

