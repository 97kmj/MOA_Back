package com.moa.notification.service.notificationService;

import java.util.List;

import com.moa.entity.Notification;
import com.moa.notification.dto.NotificationDTO;

public interface NotificationService {
	void saveAndSendNotification(String username, String message, Notification.NotificationType type);

	long getUnreadNotificationCount(String username);

	void markAsRead(Long notificationId, String username);

	List<NotificationDTO> getNotificationList(String username);
}
