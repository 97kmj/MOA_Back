package com.moa.mypage.funding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.moa.mypage.funding.dto.MyFundingResponseDTO;

public interface FundingMyPageService {

	Page<MyFundingResponseDTO> getMyContributedFunding(String username, String status, Pageable pageable);
}
