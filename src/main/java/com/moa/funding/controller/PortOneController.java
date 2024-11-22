package com.moa.funding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.funding.dto.payment.webhook.PortOneWebhookRequest;
import com.moa.funding.service.portone.PortOneWebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/moa/pontOne")
@RequiredArgsConstructor
@Slf4j
public class PortOneController {

	private final PortOneWebhookService iamportOneWebhookService;

	@PostMapping("/webhook")
	public ResponseEntity<String> handleWebhook(@RequestBody PortOneWebhookRequest request) {
		try {
			log.info("Webhook 요청: {}", request);
			iamportOneWebhookService.handleWebhook(request);
			return ResponseEntity.ok("Webhook 처리 성공 ");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Webhook 처리 실패: " + e.getMessage());
		}
	}

	// @PostMapping("/payment/prepare")
	// public ResponseEntity<?> preparePayment(@RequestBody PaymentRequest request) {
	// 	try {
	// 		// 아임포트 API 호출
	// 		HttpHeaders headers = new HttpHeaders();
	// 		headers.setContentType(MediaType.APPLICATION_JSON);
	// 		headers.setBearerAuth("5859ce7d006b9f474482f93fa01ea34a9898c5b9");
	//
	// 		Map<String, Object> body = new HashMap<>();
	// 		body.put("merchant_uid", request.getMerchantUid());
	// 		body.put("amount", request.getAmount());
	//
	// 		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
	// 		RestTemplate restTemplate = new RestTemplate();
	//
	// 		ResponseEntity<String> response = restTemplate.exchange(
	// 			"https://api.iamport.kr/payments/prepare",
	// 			HttpMethod.POST,
	// 			httpEntity,
	// 			String.class
	// 		);
	//
	// 		return ResponseEntity.ok(response.getBody());
	// 	} catch (Exception e) {
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 사전등록 실패");
	// 	}
	//
	// }

}





//
//
//
// @PostMapping("/payment/prepare")
// public ResponseEntity<?> preparePayment(@RequestBody PaymentRequest request) {
// 	try {
// 		// 아임포트 API 호출
// 		HttpHeaders headers = new HttpHeaders();
// 		headers.setContentType(MediaType.APPLICATION_JSON);
// 		headers.setBearerAuth("7b7d473783fe1bd57494da35b01da926b12a5681");
//
// 		Map<String, Object> body = new HashMap<>();
// 		body.put("merchant_uid", request.getMerchantUid());
// 		body.put("amount", request.getAmount());
//
// 		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
// 		RestTemplate restTemplate = new RestTemplate();
//
// 		ResponseEntity<String> response = restTemplate.exchange(
// 			"https://api.iamport.kr/payments/prepare",
// 			HttpMethod.POST,
// 			httpEntity,
// 			String.class
// 		);
//
// 		return ResponseEntity.ok(response.getBody());
// 	} catch (Exception e) {
// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 사전등록 실패");
// 	}
//
// }
//
// }




