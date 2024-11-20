package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.FundingContribution;

public interface FundingContributionRepository extends JpaRepository<FundingContribution, Long> {

}
