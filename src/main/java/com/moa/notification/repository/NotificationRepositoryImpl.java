package com.moa.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.moa.entity.Notification;
import com.moa.entity.QNotification;
import com.moa.notification.dto.NotificationDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public NotificationRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public long countUnreadNotifications(String username) {
		QNotification notification = QNotification.notification;

		return Optional.ofNullable(queryFactory
			.select(notification.count())
			.from(notification)
			.where(
				notification.user.username.eq(username)
					.and(notification.isRead.eq(false))
			)
			.fetchOne()).orElse(0L);
	}

	@Override
	public List<Notification> findUnreadNotifications(String username) {
		QNotification notification = QNotification.notification;

		return queryFactory
			.selectFrom(notification)
			.where(
				notification.user.username.eq(username)
					.and(notification.isRead.eq(false))
			)
			.orderBy(notification.createdAt.desc())
			.fetch();
	}


}
