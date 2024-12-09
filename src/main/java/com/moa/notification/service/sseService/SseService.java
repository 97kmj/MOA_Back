package com.moa.notification.service.sseService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.moa.notification.exception.SseHeartbeatException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SseService {

	private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

	// sse 연결을 위한 메소드
	public SseEmitter subscribe(String username) {

		// 10분 연결
		log.info("SSE clients.toString(): {}", clients.toString());

		SseEmitter emitter = new SseEmitter(0L);

		clients.put(username, emitter);

		emitter.onCompletion(() -> clients.remove(username));// 연결 종료시 클라이언트 맵에서 제거
		emitter.onTimeout(() -> clients.remove(username)); // 타임아웃시 클라이언트 맵에서 제거
		emitter.onError((e) -> {
			log.warn("SSE connection error for user {}: {}", username, e.getMessage(), e);
			clients.remove(username); // 에러시 클라이언트 맵에서 제거
		});

		// 연결 직후 더미 데이터 전송
		try {
			emitter.send(SseEmitter.event()
				.name("init") // 이벤트 이름
				.data("Hello " + username + "! This is a test notification.") // 데이터
				.id(String.valueOf(System.currentTimeMillis())) // 이벤트 ID (선택)
			);
			log.debug("Initial test message sent to user: {}", username);
		} catch (IOException e) {
			log.error("Error sending initial test message to user {}: {}", username, e.getMessage());
			clients.remove(username);
		}

		return emitter;
	}

	public void sendNotification(String username, String message) {
		SseEmitter emitter = clients.get(username);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event()
					.name("notification")
					.data(message));
			} catch (Exception e) {
				clients.remove(username);
			}
		}
	}

	public void sendUnreadCount(String username,long unreadCount) {
		SseEmitter emitter = clients.get(username);
		if (emitter != null) {
		try {
			emitter.send(SseEmitter.event()
				.name("unreadCount")
				.data(unreadCount));
			log.debug("sendUnreadCount UserName: {}", username);
			log.debug("sendUnreadCount: {}", unreadCount);
		}catch (Exception e) {
			clients.remove(username);
		}
		}
	}

	public void unsubscribe(String username) {
		SseEmitter emitter = clients.remove(username);
		if (emitter != null) {
			emitter.complete(); // 연결 종료
			log.debug("SSE connection for user {} removed on logout.", username);
		}
	}


	@Scheduled(fixedRate = 60000) //
	public void sendHeartbeat() {
		clients.forEach((username, emitter) -> {
			try {
				emitter.send(SseEmitter.event()
					.name("heartbeat")
					.data("heartbeat")
				);
				log.debug("Heartbeat sent to: {}", username);
			} catch (IOException e) {
				clients.remove(username);
				throw new SseHeartbeatException("SSE heartbeat failed for user: " + username, e);

			}
		});
	}


}
