package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Funding;

public interface FundingRepository extends JpaRepository<Funding, Long> {

}
