package com.moa.funding.service.portone;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.moa.entity.FundingOrder;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PortOneServiceImpl implements PortOneService {

	private final IamportClient iamportClient;
	private final PortOneAuthService portOneAuthService;

	public PortOneServiceImpl(
		@Value("${iamport.api-key}") String apiKey,
		@Value("${iamport.api-secret}") String apiSecret,
		PortOneAuthService portOneAuthService
	) {
		this.iamportClient = new IamportClient(apiKey, apiSecret);
		this.portOneAuthService = portOneAuthService;
	}


	// 공통 메서드: 아임포트 서버에서 결제 정보 조회
	private Payment getPaymentInfoFromPortOne(String impUid) {
		try {
			IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
			if (response != null && response.getResponse() != null) {
				return response.getResponse();
			} else {
				throw new IllegalArgumentException("유효하지 않은 결제 ID입니다: " + impUid);
			}
		} catch (Exception e) {
			log.error("아임포트 결제 정보 조회 실패: {}", e.getMessage(), e);
			throw new RuntimeException("결제 조회 중 오류 발생: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean verifyPayment(Long amount, String impUid) {
		try {
			// 공통 로직 호출
			Payment payment = getPaymentInfoFromPortOne(impUid);

			// 결제 금액 검증
			return payment.getAmount().compareTo(BigDecimal.valueOf(amount)) == 0;
		} catch (Exception e) {
			log.error("아임포트 결제 검증 중 오류 발생: {}", e.getMessage(), e);
			return false;
		}
	}

	@Override
	public Payment getPaymentDetails(String impUid) {
		// 공통 로직 호출
		return getPaymentInfoFromPortOne(impUid);
	}


	@Override
	public boolean preparePayment(String merchantUid, BigDecimal amount) {
		try {
			// 유효한 Bearer 토큰 가져오기
			String bearerToken = portOneAuthService.getAccessToken();

			// HTTP 헤더 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(bearerToken);

			// 요청 바디 설정
			Map<String, Object> body = new HashMap<>();
			body.put("merchant_uid", merchantUid);
			body.put("amount", amount);

			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
			RestTemplate restTemplate = new RestTemplate();

			// 아임포트 API 호출
			ResponseEntity<String> response = restTemplate.exchange(
				"https://api.iamport.kr/payments/prepare",
				HttpMethod.POST,
				httpEntity,
				String.class
			);

			if (response.getStatusCode() == HttpStatus.OK) {
				log.info("결제 사전 등록 성공: {}", response.getBody());
				return true;
			} else {
				log.error("결제 사전 등록 실패: {}", response.getBody());
				return false;
			}
		} catch (Exception e) {
			log.error("결제 사전 등록 중 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("결제 사전 등록 중 오류 발생: " + e.getMessage(), e);
		}
	}

	// @Override
	// public String preparePayment(String merchantUid, BigDecimal amount) {
	// 	try {
	// 		// Step 1: 유효한 Bearer 토큰 가져오기
	// 		String bearerToken = portOneAuthService.getAccessToken();
	//
	// 		// Step 2: HTTP 헤더 설정
	// 		HttpHeaders headers = new HttpHeaders();
	// 		headers.setContentType(MediaType.APPLICATION_JSON);
	// 		headers.setBearerAuth(bearerToken);
	//
	// 		// Step 3: 요청 바디 설정
	// 		Map<String, Object> body = new HashMap<>();
	// 		body.put("merchant_uid", merchantUid);
	// 		body.put("amount", amount);
	//
	// 		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, headers);
	// 		RestTemplate restTemplate = new RestTemplate();
	//
	// 		// Step 4: 아임포트 API 호출
	// 		ResponseEntity<String> response = restTemplate.exchange(
	// 			"https://api.iamport.kr/payments/prepare",
	// 			HttpMethod.POST,
	// 			httpEntity,
	// 			String.class
	// 		);
	//
	// 		if (response.getStatusCode() == HttpStatus.OK) {
	// 			log.info("결제 사전 등록 성공: {}", response.getBody());
	// 			return merchantUid; // 성공 시 merchantUid 반환
	// 		} else {
	// 			log.error("결제 사전 등록 실패: {}", response.getBody());
	// 			throw new RuntimeException("결제 사전 등록 실패: " + response.getBody());
	// 		}
	// 	} catch (Exception e) {
	// 		log.error("결제 사전 등록 중 오류 발생: {}", e.getMessage(), e);
	// 		throw new RuntimeException("결제 사전 등록 중 오류 발생: " + e.getMessage(), e);
	// 	}
	// }
	//



	@Override
	public void refundOrder(FundingOrder order) {
		try {
			iamportClient.cancelPaymentByImpUid(
				new CancelData(order.getImpUid(), true, BigDecimal.valueOf(order.getTotalAmount()))
			);
			log.info("환불 성공: orderId={}, impUid={}, amount={}",
				order.getFundingOrderId(), order.getImpUid(), order.getTotalAmount());
		} catch (IamportResponseException | IOException e) {
			log.error("환불 실패: orderId={}, error={}", order.getFundingOrderId(), e.getMessage());
			throw new RuntimeException("환불 처리 중 오류 발생: " + e.getMessage(), e);
		}
	}

}
