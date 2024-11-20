package com.moa.funding.service.fundingImplements;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.moa.funding.service.IamPortService;
@SpringBootTest
class IamPortServiceImplTest {

	@Autowired
	private IamPortService iamportService;

	@Test
	void testVerifyPaymentSuccess() {
		String impUid = "imp_002520757282";
		Long expectedAmount = 100L;

		boolean isVerified = iamportService.verifyPayment(expectedAmount, impUid);
		assertTrue(isVerified, "결제가 성공적으로 검증되지 않았습니다.");
	}

	@Test
	void testVerifyPaymentFail() {
		String impUid = "wrong_imp_uid";
		Long expectedAmount = 100L;

		boolean isVerified = iamportService.verifyPayment(expectedAmount, impUid);
		assertFalse(isVerified, "유효하지 않은 결제가 검증되었습니다.");
	}


}