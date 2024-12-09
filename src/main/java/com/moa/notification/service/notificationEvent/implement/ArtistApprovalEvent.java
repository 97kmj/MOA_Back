package com.moa.notification.service.notificationEvent.implement;

import com.moa.entity.Notification;
import com.moa.notification.service.notificationEvent.NotificationEvent;

public class ArtistApprovalEvent implements NotificationEvent {

	private final String username;

	public ArtistApprovalEvent(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String createMessage() {
		return "축하합니다! 회원 " + username + "님이 작가로 승인되었습니다.";
	}

	@Override
	public Notification.NotificationType getNotificationType() {
		return Notification.NotificationType.SYSTEM;
	}
}
