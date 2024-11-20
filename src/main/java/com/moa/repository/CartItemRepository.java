package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
