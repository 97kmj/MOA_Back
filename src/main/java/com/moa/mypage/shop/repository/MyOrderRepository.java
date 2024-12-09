package com.moa.mypage.shop.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Order;
import com.moa.entity.QOrder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyOrderRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<Order> findOrderIdByDate(Date startDate, Date endDate) {
		QOrder order = QOrder.order;
		return jpaQueryFactory.select(order)
							.from(order)
							.where(order.paymentDate.between(new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())))
							.fetch();
	}
}
