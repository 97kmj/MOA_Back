package com.moa.funding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.portone.PortOneService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FundingPaymentController {

	private final FundingPaymentService fundingPaymentService;
	private final PortOneService portOneService;


	@PostMapping("/payment/prepare")
	public ResponseEntity<String> preparePayment(@RequestBody PaymentRequest request) {
		boolean success = portOneService.preparePayment(request.getMerchantUid(), request.getAmount());
		if (success) {
			return ResponseEntity.ok("결제 사전 등록 성공");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 사전 등록 실패");
		}
	}


	@PostMapping("/payment")
	public ResponseEntity<Void> processPayment(@RequestBody PaymentRequest paymentRequest) {
		log.info("impUid: {}, paymentRequest: {}", paymentRequest.getImpUid(), paymentRequest);
		try {
			fundingPaymentService.processFundingContribution(paymentRequest.getImpUid(), paymentRequest);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			log.error("결제 처리 중 오류 발생: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}


}

