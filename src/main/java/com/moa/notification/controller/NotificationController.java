package com.moa.notification.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.moa.notification.dto.NotificationDTO;
import com.moa.notification.service.notificationService.NotificationService;
import com.moa.notification.service.sseService.SseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
	private final SseService sseService;
	private final NotificationService notificationService;

	@GetMapping(value = "/subscribe/{username}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> subscribe(@PathVariable String username) {
		log.info("SSE subscription request received for user: {}", username);

		// SseEmitter 생성
		SseEmitter emitter = sseService.subscribe(username);

		return ResponseEntity.ok()
			.header("Content-Type", "text/event-stream")
			.header("Cache-Control", "no-cache")
			.header("Connection", "keep-alive")
			.body(emitter);
	}

	@GetMapping("/count")
	public ResponseEntity<Long> getUnreadNotificationCount(String username) {
		// log.info("읽지않은 알림 개수 요청: {}", username);
		long unreadNotificationCount = notificationService.getUnreadNotificationCount(username);
		// log.info("읽지않은 알림 개수: {}", unreadNotificationCount);
		return ResponseEntity.ok(unreadNotificationCount);
	}

	@GetMapping("/notifications")
	public ResponseEntity<List<NotificationDTO>> getNotifications(String username) {
		// log.info("알림 리스트 요청: {}", username);
		List<NotificationDTO> notifications = notificationService.getNotificationList(username);
		// log.info("알림 리스트: {}", notifications);
		return ResponseEntity.ok(notifications);
	}


	@PostMapping("/markAsRead/{notificationId}")
	public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId, String username) {
		notificationService.markAsRead(notificationId, username);
		return ResponseEntity.noContent().build();


}

	@DeleteMapping("/unsubscribe/{username}")
	public ResponseEntity<Void> unsubscribe(@PathVariable String username) {
		log.info("SSE unsubscription request received for user: {}", username);
		sseService.unsubscribe(username);
		return ResponseEntity.noContent().build();
	}

}
