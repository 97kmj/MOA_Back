package com.moa.mypage.funding.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.mypage.funding.dto.FundingOrderDetailResponseDTO;

public interface MyPageFundingRepositoryCustom {
	Page<FundingOrder> findMyContributedFunding(String username, String status, Pageable pageable);

	FundingOrder findFundingOrder(Long fundingOrderId);
	List<FundingContribution> findFundingContributions(Long fundingOrderId);

}
