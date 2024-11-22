package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.FundingOrder;

public interface FundingOrderRepository extends JpaRepository<FundingOrder, Long> {

	boolean existsByImpUid(String impUid);
}
