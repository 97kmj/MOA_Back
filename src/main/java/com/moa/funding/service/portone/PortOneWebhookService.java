package com.moa.funding.service.portone;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.funding.dto.payment.webhook.PortOneCustomData;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.webhook.PortOneWebhookRequest;
import com.moa.funding.mapper.FundingPaymentMapper;
import com.moa.funding.service.FundingPaymentService;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortOneWebhookService {

	private final PortOneService portOneService;
	private final FundingPaymentService fundingPaymentService;
	private final ObjectMapper jacksonObjectMapper;

	public void handleWebhook(PortOneWebhookRequest request) {
		try {
			// Step 1: 아임포트 서버에서 결제 정보 가져오기
			Payment payment = portOneService.getPaymentDetails(request.getImpUid());
			log.info("아임포트 결제 정보: {}", payment);

			// Step 2: custom_data 파싱
			PortOneCustomData customData = parseCustomData(payment.getCustomData());
			log.info("Parsed custom_data: {}", customData);

			// Step 3: PaymentRequest 생성
			PaymentRequest paymentRequest = FundingPaymentMapper.toPaymentRequest(request, payment);
			log.info("PaymentRequest 생성: {}", paymentRequest);

			// Step 4: 결제 처리 (DB 저장 포함)
			fundingPaymentService.completeFundingContribution(request.getImpUid(), paymentRequest);
			log.info("결제 처리 완료");

		} catch (Exception e) {
			log.error("Webhook 처리 중 오류 발생", e);
			throw new RuntimeException("Webhook 처리 실패", e);
		}
	}

	public PortOneCustomData parseCustomData(String customDataJson) {
		try {
			log.info("Parsing JSON: {}", customDataJson);
			return jacksonObjectMapper.readValue(customDataJson, PortOneCustomData.class);
		} catch (Exception e) {
			log.error("Failed to parse custom data JSON: {}", customDataJson, e);
			throw new RuntimeException("Custom data 파싱 실패", e);
		}
	}
}