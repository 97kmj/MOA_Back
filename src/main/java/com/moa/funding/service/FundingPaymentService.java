package com.moa.funding.service;

import com.moa.funding.dto.payment.PaymentRequest;

public interface FundingPaymentService {
	void prepareFundingOrder(PaymentRequest paymentRequest,String userName);

	// void confirmFundingContribution(String impUid);


	// void cancelFundingContribution(List<RewardRequest> rewardList);

	void completeFundingContribution(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(PaymentRequest paymentRequest);


}

