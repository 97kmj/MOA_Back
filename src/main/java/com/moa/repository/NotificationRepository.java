package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
