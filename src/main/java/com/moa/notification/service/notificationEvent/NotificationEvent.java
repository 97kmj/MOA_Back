package com.moa.notification.service.notificationEvent;

import com.moa.entity.Notification;

public interface NotificationEvent {
	String getUsername();
	String createMessage();
	Notification.NotificationType getNotificationType();

}
