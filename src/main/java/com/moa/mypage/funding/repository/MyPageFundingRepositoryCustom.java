package com.moa.mypage.funding.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;

public interface MyPageFundingRepositoryCustom {
	Page<FundingOrder> findMyContributedFunding(String username, String status, Pageable pageable);

	FundingOrder findFundingOrderWithUser(Long fundingOrderId);
	List<FundingContribution> findFundingContributions(Long fundingOrderId);

	FundingOrder findFundingOrderWithFunding(Long fundingOrderId);

	Page<Funding> findMyRegisteredFunding(String username, String status, Pageable pageable);

	Page<FundingContribution> findContributionsForMyFunding(Long fundingId, Pageable pageable);

	Page<FundingOrder> findFundingOrdersForFunding(Long fundingId, Pageable pageable);

	List<FundingContribution> findContributionsByFundingOrderId(Long fundingOrderId);
}
