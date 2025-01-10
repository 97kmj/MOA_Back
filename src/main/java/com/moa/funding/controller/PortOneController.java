package com.moa.funding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.webhook.PortOneWebhookRequest;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.RewardStockCache;
import com.moa.funding.service.portone.PortOneService;
import com.moa.funding.service.portone.PortOneWebhookService;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/moa/pontOne")
@RequiredArgsConstructor
@Slf4j
public class PortOneController {
	private final PortOneService portOneService; // 기존 서비스 활용
	private final RewardStockCache rewardStockCacheFromRedis;
	private final FundingPaymentService fundingPaymentService;

	@PostMapping("/webhook")
	public ResponseEntity<Void> handleWebhook(@RequestBody PortOneWebhookRequest webhookRequest) {
		try {
			log.info("웹훅 요청: {}", webhookRequest);

			Payment payment = portOneService.getPaymentDetails(webhookRequest.getImpUid());

			if (!"paid".equals(payment.getStatus())) {
				log.warn("결제 상태가 유효하지않음 : {}", payment);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}

			PaymentRequest paymentRequest = rewardStockCacheFromRedis.createPaymentRequestFromRedis(
				payment.getMerchantUid());

			fundingPaymentService.completeFundingContribution(webhookRequest.getImpUid(), paymentRequest);

			log.info("웹훅 처리 완료 - impUid: {}, merchantUid: {}", webhookRequest.getImpUid(),
				webhookRequest.getMerchantUid());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("웹훅 처리 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}

	}

}


