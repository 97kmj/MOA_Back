package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
