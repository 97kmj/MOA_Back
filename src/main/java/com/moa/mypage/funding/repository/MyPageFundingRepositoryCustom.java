package com.moa.mypage.funding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moa.entity.FundingOrder;

public interface MyPageFundingRepositoryCustom {
	Page<FundingOrder> findMyContributedFunding(String username, String status, Pageable pageable);
}
