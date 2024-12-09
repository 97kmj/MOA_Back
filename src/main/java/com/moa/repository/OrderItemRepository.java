package com.moa.repository;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	Page<OrderItem> findByOrder_User_UsernameAndOrder_PaymentDateBetween(String username, Timestamp startDate, Timestamp endDate, Pageable pageable);

}
