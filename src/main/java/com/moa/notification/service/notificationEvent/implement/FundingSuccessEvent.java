package com.moa.notification.service.notificationEvent.implement;

import com.moa.entity.Notification;
import com.moa.notification.service.notificationEvent.NotificationEvent;

public class FundingSuccessEvent implements NotificationEvent {
	@Override
	public String getUsername() {
		return "";
	}

	@Override
	public String createMessage() {
		return "";
	}

	@Override
	public Notification.NotificationType getNotificationType() {
		return null;
	}
}
