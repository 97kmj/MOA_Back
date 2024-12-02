package com.moa.funding.service;

import com.moa.funding.dto.payment.PaymentRequest;

public interface FundingPaymentService {
	void prepareFundingOrder(PaymentRequest paymentRequest);

	// void confirmFundingContribution(String impUid);


	// void cancelFundingContribution(List<RewardRequest> rewardList);

	void processFundingContribution(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(PaymentRequest paymentRequest);


}

