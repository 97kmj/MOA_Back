package com.moa.funding.service;

import com.moa.funding.dto.payment.PaymentRequest;

public interface FundingPaymentService {
	void processFundingContribution(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(PaymentRequest paymentRequest);


}

