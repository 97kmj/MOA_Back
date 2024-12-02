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
		return createErrorResponse(HttpStatus.BAD_REQUEST, "REWARD_STOCK_ERROR", ex.getMessage());
	}

	@ExceptionHandler(FundingPeriodException.class)
	public ResponseEntity<Map<String, String>> fundingPeriodError(FundingPeriodException ex) {
		return createErrorResponse(HttpStatus.BAD_REQUEST, "FUNDING_PERIOD_ERROR", ex.getMessage());
	}

	@ExceptionHandler(RewardLimitException.class)
	public ResponseEntity<Map<String, String>> rewardLimitError(RewardLimitException ex) {
		return createErrorResponse(HttpStatus.BAD_REQUEST, "REWARD_LIMIT_ERROR", ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> RuntimeException(RuntimeException ex) {
		return createErrorResponse(HttpStatus.BAD_REQUEST, "RUNTIME_ERROR", ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> GenericException(Exception ex) {
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "GENERIC_ERROR", "오류 발생: " + ex.getMessage());
	}

	private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String error, String message) {
		Map<String, String> response = new HashMap<>();
		response.put("error", error);
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}
}
