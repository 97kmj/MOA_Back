package com.moa.funding.service.portone;

import java.math.BigDecimal;

import com.moa.entity.FundingOrder;
import com.siot.IamportRestClient.response.Payment;

public interface PortOneService {
	boolean verifyPayment(Long amount, String impUid);

	Payment getPaymentDetails(String impUid);

	boolean preparePayment(String merchantUid, BigDecimal amount);

	void refundOrder(FundingOrder order);
}
