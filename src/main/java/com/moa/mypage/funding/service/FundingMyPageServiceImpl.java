package com.moa.mypage.funding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moa.entity.FundingOrder;
import com.moa.mypage.funding.dto.MyFundingResponseDTO;
import com.moa.mypage.funding.repository.MyPageFundingRepositoryCustom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundingMyPageServiceImpl implements FundingMyPageService {
	private final MyPageFundingRepositoryCustom fundingMyPageRepository;


	@Override
	public Page<MyFundingResponseDTO> getMyContributedFunding(String username, String status, Pageable pageable) {
		log.info("username: {}, status: {}, pageable: {}", username, status, pageable);

		Page<FundingOrder> fundingOrders = fundingMyPageRepository.findMyContributedFunding(username, status, pageable);

		Page<MyFundingResponseDTO> responseDTOs = fundingOrders.map(this::toMyFundingResponseDTO);

		log.info("responseDTOs: {}", responseDTOs);
		return responseDTOs;
	}


	private MyFundingResponseDTO toMyFundingResponseDTO(FundingOrder fundingOrder) {
		return MyFundingResponseDTO.builder()
			.fundingOrderId(fundingOrder.getFundingOrderId())
			.fundingId(fundingOrder.getFunding().getFundingId())
			.fundingTitle(fundingOrder.getFunding().getTitle())
			.fundingImage(fundingOrder.getFunding().getFundingMainImageUrl())
			.fundingStatus(fundingOrder.getFunding().getFundingStatus().name())
			.goalAmount(fundingOrder.getFunding().getGoalAmount())
			.startDate(fundingOrder.getFunding().getStartDate())
			.endDate(fundingOrder.getFunding().getEndDate())
			.build();
	}

}
