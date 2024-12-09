// package com.moa.notification.exception;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//
// import lombok.extern.slf4j.Slf4j;
//
// @RestControllerAdvice
// @Slf4j
// public class GlobalExceptionHandler {
//
//
// 	}
//
//
// 	private void logException(Exception ex) {
// 		StackTraceElement[] stackTrace = ex.getStackTrace();
// 		StackTraceElement relevantTrace = stackTrace.length > 0 ? stackTrace[0] : null;
//
// 		if (relevantTrace != null) {
// 			log.error("예외 발생: {}, 발생 위치: {}.{}({}:{}) - 메시지: {}",
// 				ex.getClass().getName(),
// 				relevantTrace.getClassName(),
// 				relevantTrace.getMethodName(),
// 				relevantTrace.getFileName(),
// 				relevantTrace.getLineNumber(),
// 				ex.getMessage()
// 			);
// 		} else {
// 			log.error("스택 트레이스 없음 - 메시지: {}", ex.getMessage());
// 		}
// 	}
// }