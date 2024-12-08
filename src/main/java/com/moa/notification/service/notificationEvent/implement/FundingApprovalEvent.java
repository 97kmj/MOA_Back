package com.moa.notification.service.notificationEvent.implement;

import com.moa.entity.Notification;
import com.moa.notification.service.notificationEvent.NotificationEvent;

public class FundingApprovalEvent implements NotificationEvent {
	private final String username;
	private final String fundingName;

	public FundingApprovalEvent(String username, String fundingName) {
		this.username = username;
		this.fundingName = fundingName;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String createMessage() {
		return "회원 " + username + "님의 펀딩 '" + fundingName + "'이 승인되었습니다.";
	}

	@Override
	public Notification.NotificationType getNotificationType() {
		return Notification.NotificationType.FUNDING;
	}
}
