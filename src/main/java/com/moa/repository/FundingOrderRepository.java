package com.moa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.FundingOrder;

public interface FundingOrderRepository extends JpaRepository<FundingOrder, Long> {

	boolean existsByImpUid(String impUid);

	//타입을 옵셔널로 바꿔줘
	Optional <FundingOrder> findByImpUid(String impUid);

	Optional <FundingOrder>findByMerchantUid(String merchantUid);
}
