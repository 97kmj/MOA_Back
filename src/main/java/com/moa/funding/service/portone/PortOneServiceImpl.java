package com.moa.funding.service.portone;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
			Payment payment = getPaymentInfoFromPortOne(impUid);

			// 결제 금액 검증
			if (payment.getAmount().compareTo(BigDecimal.valueOf(amount)) != 0) {
				log.warn("결제 금액 불일치 - impUid: {}, 요청 금액: {}, 실제 결제 금액: {}", impUid, amount, payment.getAmount());
				return false;
			}
			log.info("결제 금액 일치 - impUid: {}, 금액: {}", impUid, amount);
			return true;
		} catch (Exception e) {
			log.error("결제 검증 중 예외 발생 - impUid: {}, 메시지: {}", impUid, e.getMessage());
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


	@Override
	public Payment getPaymentByMerchantUid(String merchantUid) {
		try {
			// Bearer 토큰 및 요청 생성
			String bearerToken = portOneAuthService.getAccessToken();
			String url = "https://api.iamport.kr/payments/find/" + merchantUid;

			HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Authorization", "Bearer " + bearerToken)
				.GET()
				.build();

			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Gson을 사용하여 응답 파싱
			Gson gson = new Gson();
			JsonObject rootNode = JsonParser.parseString(response.body()).getAsJsonObject();
			JsonObject responseNode = rootNode.getAsJsonObject("response");

			if (responseNode == null) {
				throw new IllegalArgumentException("결제 정보를 찾을 수 없습니다: " + merchantUid);
			}

			// Payment 객체로 변환하여 반환
			return gson.fromJson(responseNode, Payment.class);

		} catch (Exception e) {
			log.error("merchantUid로 결제 정보 조회 중 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("merchantUid로 결제 정보 조회 중 오류 발생: " + e.getMessage(), e);
		}
	}




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
