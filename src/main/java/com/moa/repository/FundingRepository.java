package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Funding;
import com.moa.entity.Funding.ApprovalStatus;

public interface FundingRepository extends JpaRepository<Funding, Long> {
	List<Funding> findByApprovalStatus(ApprovalStatus status);
}
