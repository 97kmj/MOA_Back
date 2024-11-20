package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
