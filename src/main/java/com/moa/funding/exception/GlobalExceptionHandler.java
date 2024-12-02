package com.moa.funding.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RewardStockException.class)
	public ResponseEntity<Map<String, String>> rewardStockError(RewardStockException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", "REWARD_STOCK_ERROR");
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}


	// 펀딩 기간 예외 처리
	@ExceptionHandler(FundingPeriodException.class)
	public ResponseEntity<Map<String, String>> fundingPeriodError(FundingPeriodException ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", "FUNDING_PERIOD_ERROR");
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> fundingPeriodError(Exception ex) {
		Map<String, String> response = new HashMap<>();
		response.put("error", "GENERIC_ERROR");
		response.put("message", "결제 준비 중 오류 발생: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
