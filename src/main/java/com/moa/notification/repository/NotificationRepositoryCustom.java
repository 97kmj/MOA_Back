package com.moa.notification.repository;

import java.util.List;

import com.moa.entity.Notification;
import com.moa.notification.dto.NotificationDTO;

public interface NotificationRepositoryCustom {
	long countUnreadNotifications(String username);

	List<Notification> findUnreadNotifications(String username);
}
