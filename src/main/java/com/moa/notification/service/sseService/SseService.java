package com.moa.notification.service.sseService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SseService {

	private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

	// sse 연결을 위한 메소드
	public SseEmitter subscribe(String username) {

		SseEmitter oldEmitter = clients.remove(username);
		if (oldEmitter != null) {
			oldEmitter.complete();
		}

		// 10분 연결
		SseEmitter emitter = new SseEmitter(600_000L);

		emitter.onCompletion(() -> clients.remove(username));// 연결 종료시 클라이언트 맵에서 제거
		emitter.onTimeout(() -> clients.remove(username)); // 타임아웃시 클라이언트 맵에서 제거
		emitter.onError((e) -> {
			log.error("SSE connection error for user {}: {}", username, e.getMessage(), e);
			clients.remove(username); // 에러시 클라이언트 맵에서 제거
		});


		clients.put(username, emitter);
		return emitter;
	}

	public void sendNotification(String username, String message) {
		SseEmitter emitter = clients.get(username);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().data(message));
			} catch (Exception e) {
				clients.remove(username);
			}
		}
	}

}
