package com.moa.notification.service.notificationService;

import com.moa.entity.Notification;

public interface NotificationService {
	void saveAndSendNotification(String username, String message, Notification.NotificationType type);
}
