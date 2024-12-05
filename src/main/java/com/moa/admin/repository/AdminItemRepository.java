package com.moa.admin.repository;

import com.moa.entity.QOrderItem;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.OrderItem;
import com.moa.entity.OrderItem.ShippingStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminItemRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<OrderItem> findOrderItemList() {
		QOrderItem orderItem = QOrderItem.orderItem;
		return jpaQueryFactory.selectFrom(orderItem)
				.where(orderItem.shippingStatus.eq(ShippingStatus.WAITING)
						.or(orderItem.shippingStatus.eq(ShippingStatus.INSPECTION)))
				.orderBy(orderItem.order.orderId.desc())
				.fetch();
	}
	
	
}
