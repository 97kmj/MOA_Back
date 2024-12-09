package com.moa.notification.service.notificationEventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.moa.notification.service.notificationEvent.NotificationEvent;
import com.moa.notification.service.notificationService.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
	private final NotificationService notificationService;

	@EventListener
	public void processEvent(NotificationEvent event) {
		String message = event.createMessage();
		notificationService.saveAndSendNotification(
					event.getUsername(),
					message,
					event.getNotificationType()
		);

	}

}


