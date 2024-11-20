package com.moa.funding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.funding.dto.FundingOrderDTO;
import com.moa.funding.dto.PaymentRequest;
import com.moa.funding.service.FundingPaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/funding")
public class FundingPaymentController {

	private final FundingPaymentService fundingPaymentService;

	@Autowired
	public FundingPaymentController(FundingPaymentService fundingPaymentService) {
		this.fundingPaymentService = fundingPaymentService;
	}

	@PostMapping("payment")
	public ResponseEntity<FundingOrderDTO> processPayment(
		@RequestBody PaymentRequest paymentRequest) {

		log.info("impUid: {}, paymentRequest: {}",paymentRequest.getImpUid() , paymentRequest);
		try {
			FundingOrderDTO order = fundingPaymentService.processPayment(paymentRequest.getImpUid(), paymentRequest);
			return ResponseEntity.ok(order);
		} catch (RuntimeException e) {
			log.error("결제 처리 중 오류 발생: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}

