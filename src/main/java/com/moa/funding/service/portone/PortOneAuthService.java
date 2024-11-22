package com.moa.funding.service.portone;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PortOneAuthService {



	@Value("${iamport.api-key}")
	private String apiKey;

	@Value("${iamport.api-secret}")
	private String apiSecret;

	private String accessToken; // 현재 액세스 토큰
	private long tokenExpirationTime; // 토큰 만료 시각 (epoch time)

	// 액세스 토큰 발급 메서드
	public String getAccessToken() {
		if (isTokenExpired()) {
			// 토큰이 만료되었으면 새로 발급
			return refreshToken();
		}
		return accessToken;
	}

	// 토큰 만료 여부 확인
	private boolean isTokenExpired() {
		return accessToken == null || System.currentTimeMillis() >= tokenExpirationTime;
	}

	// 토큰 갱신
	private synchronized String refreshToken() {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = new HashMap<>();
		body.put("imp_key", apiKey);
		body.put("imp_secret", apiSecret);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(
				"https://api.iamport.kr/users/getToken",
				request,
				Map.class
			);

			if (response.getStatusCode() == HttpStatus.OK) {
				Map<String, Object> responseBody = response.getBody();
				Map<String, Object> responseData = (Map<String, Object>) responseBody.get("response");

				// 토큰과 만료 시간 저장
				accessToken = (String) responseData.get("access_token");
				int expiresIn = (int) responseData.get("expired_at");
				tokenExpirationTime = expiresIn * 1000L; // 만료 시간은 초 단위 -> 밀리초로 변환

				return accessToken;
			} else {
				throw new RuntimeException("토큰 발급 실패: " + response.getStatusCode());
			}
		} catch (Exception e) {
			throw new RuntimeException("아임포트 인증 요청 중 오류 발생: " + e.getMessage(), e);
		}
	}
}
