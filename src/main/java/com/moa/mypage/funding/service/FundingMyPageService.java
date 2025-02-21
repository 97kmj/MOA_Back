package com.moa.mypage.funding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moa.mypage.funding.dto.FundingContributionWithFundingDTO;
import com.moa.mypage.funding.dto.FundingOrderDetailResponseDTO;
import com.moa.mypage.funding.dto.MyFundingResponseDTO;

public interface FundingMyPageService {

	Page<MyFundingResponseDTO> getMyContributedFunding(String username, String status, Pageable pageable);

	FundingOrderDetailResponseDTO getFundingOrderDetail(Long fundingOrderId);

	Page<MyFundingResponseDTO> getMyRegisteredFunding(String username, String status, Pageable pageable);

	Page<FundingContributionWithFundingDTO> getContributionsForMyFunding(Long fundingId, Pageable pageable);
}
