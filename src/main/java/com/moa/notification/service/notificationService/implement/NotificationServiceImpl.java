package com.moa.notification.service.notificationService.implement;

import org.springframework.stereotype.Service;

import com.moa.entity.Notification;
import com.moa.entity.User;
import com.moa.notification.service.notificationService.NotificationService;
import com.moa.notification.service.sseService.SseService;
import com.moa.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final SseService sseService;
	private final NotificationRepository notificationRepository;

	@Override
	public void saveAndSendNotification(String username, String message, Notification.NotificationType type) {
		Notification notification = Notification.builder()
			.user(User.builder().username(username).build())
			.message(message)
			.notificationType(type)
			.isRead(false)
			.build();

		notificationRepository.save(notification);

		sseService.sendNotification(username, message);
	}
}
