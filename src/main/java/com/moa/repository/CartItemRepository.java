package com.moa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moa.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem ci WHERE ci.cart.cartId IN :cartIds")
	void deleteAllByCartIds(@Param("cartIds") List<Long> cartIds);
}
