package com.moa.notification.service.notificationService.implement;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moa.entity.Notification;
import com.moa.entity.User;
import com.moa.notification.dto.NotificationDTO;
import com.moa.notification.repository.NotificationRepositoryCustom;
import com.moa.notification.service.notificationService.NotificationService;
import com.moa.notification.service.sseService.SseService;
import com.moa.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final SseService sseService;
	private final NotificationRepository notificationRepository;
	private final NotificationRepositoryCustom notificationRepositoryCustom;

	@Override
	public void saveAndSendNotification(String username, String message, Notification.NotificationType type) {
		Notification notification = Notification.builder()
			.user(User.builder().username(username).build())
			.message(message)
			.notificationType(type)
			.createdAt(new Timestamp(System.currentTimeMillis()))
			.isRead(false)
			.build();

		notificationRepository.save(notification);

		sseService.sendNotification(username, message);

		long unreadNotificationCount = getUnreadNotificationCount(username);
		sseService.sendUnreadCount(username, unreadNotificationCount);
	}

	@Override
	public long getUnreadNotificationCount(String username) {
		return notificationRepositoryCustom.countUnreadNotifications(username);
	}

	@Override
	public void markAsRead(Long notificationId, String username) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new IllegalArgumentException("Notification not found"));
		notification.setIsRead(true);
		notificationRepository.save(notification);

		long unreadCount = getUnreadNotificationCount(username);
		sseService.sendUnreadCount(username, unreadCount);

	}

	@Override
	public List<NotificationDTO> getNotificationList(String username) {
		List<Notification> unreadNotifications = notificationRepositoryCustom.findUnreadNotifications(username);

		return unreadNotifications.stream()
			.map(this::toNotificationDTO)
			.collect(Collectors.toList());
	}

	private NotificationDTO toNotificationDTO(Notification notification) {
		return NotificationDTO.builder()
			.id(notification.getNotificationId())
			.message(notification.getMessage())
			.isRead(notification.getIsRead())
			.createdAt(notification.getCreatedAt().toLocalDateTime())
			.build();

	}

}
