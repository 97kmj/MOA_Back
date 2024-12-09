package com.moa.funding.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.moa.notification.exception.SseHeartbeatException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


	@ExceptionHandler(RewardStockException.class)
	public ResponseEntity<Map<String, String>> rewardStockError(RewardStockException ex) {
		logException(ex); // 예외 로깅
		return createErrorResponse(HttpStatus.BAD_REQUEST, "REWARD_STOCK_ERROR", ex.getMessage());
	}

	@ExceptionHandler(FundingPeriodException.class)
	public ResponseEntity<Map<String, String>> fundingPeriodError(FundingPeriodException ex) {
		logException(ex); // 예외 로깅
		return createErrorResponse(HttpStatus.BAD_REQUEST, "FUNDING_PERIOD_ERROR", ex.getMessage());
	}

	@ExceptionHandler(RewardLimitException.class)
	public ResponseEntity<Map<String, String>> rewardLimitError(RewardLimitException ex) {
		logException(ex); // 예외 로깅
		return createErrorResponse(HttpStatus.BAD_REQUEST, "REWARD_LIMIT_ERROR", ex.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, String>> RuntimeException(RuntimeException ex) {
		logException(ex); // 예외 로깅
		return createErrorResponse(HttpStatus.BAD_REQUEST, "RUNTIME_ERROR", ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> GenericException(Exception ex) {
		logException(ex); // 예외 로깅
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "GENERIC_ERROR", "오류 발생: " + ex.getMessage());
	}

	private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String error, String message) {
		Map<String, String> response = new HashMap<>();
		response.put("error", error);
		response.put("message", message);
		return ResponseEntity.status(status).body(response);
	}


	@ExceptionHandler(ClientAbortException.class)
	public void handleClientAbortException(ClientAbortException ex) {
		log.info("Client aborted connection: {}", ex.getMessage());
	}

	@ExceptionHandler(SseHeartbeatException.class)
	public ResponseEntity<String> handleSseConnectionException(SseHeartbeatException ex) {
		log.warn("SSE connection issue: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SSE connection error occurred.");
	}

	private void logException(Exception ex) {
		StackTraceElement[] stackTrace = ex.getStackTrace();
		StackTraceElement relevantTrace = stackTrace.length > 0 ? stackTrace[0] : null;

		if (relevantTrace != null) {
			log.error("예외 발생: {}, 발생 위치: {}.{}({}:{}) - 메시지: {}",
				ex.getClass().getName(),
				relevantTrace.getClassName(),
				relevantTrace.getMethodName(),
				relevantTrace.getFileName(),
				relevantTrace.getLineNumber(),
				ex.getMessage()
			);
		} else {
			log.error("스택 트레이스 없음 - 메시지: {}", ex.getMessage());
		}
	}
}