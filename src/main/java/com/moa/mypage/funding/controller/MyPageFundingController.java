package com.moa.mypage.funding.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.mypage.funding.dto.MyFundingResponseDTO;
import com.moa.mypage.funding.service.FundingMyPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/myPage/funding")
@RequiredArgsConstructor
public class MyPageFundingController {
	private final FundingMyPageService fundingMyPageService;


	@GetMapping("/contributedFunding")
	public ResponseEntity<Page<MyFundingResponseDTO>> getMyContributedFunding(
		@RequestParam String username,
		@RequestParam String status,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		Page<MyFundingResponseDTO> fundingOrders = fundingMyPageService.getMyContributedFunding(username, status, pageable);
		return ResponseEntity.ok(fundingOrders);
	}

}
