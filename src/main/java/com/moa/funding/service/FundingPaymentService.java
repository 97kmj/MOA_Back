package com.moa.funding.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;

public interface FundingPaymentService {
	void prepareFundingContribution(PaymentRequest paymentRequest);

	// void confirmFundingContribution(String impUid);


	// void cancelFundingContribution(List<RewardRequest> rewardList);

	void processFundingContribution(String impUid, PaymentRequest paymentRequest);

	void updateFundingCurrentAmount(PaymentRequest paymentRequest);


}

