package com.moa.mypage.funding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.mypage.funding.dto.FundingOrderDetailResponseDTO;
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

	@Override
	public FundingOrderDetailResponseDTO getFundingOrderDetail(Long fundingOrderId) {
		log.info("fundingOrderId: {}", fundingOrderId);
		FundingOrder fundingOrder =  fundingMyPageRepository.findFundingOrder(fundingOrderId);

		if(fundingOrder == null) {
			throw new IllegalArgumentException("해당 주문이 존재하지 않습니다.");
		}

		List<FundingContribution> contributions = fundingMyPageRepository.findFundingContributions(fundingOrderId);

		log.info("fundingOrder: {}", fundingOrder);
		log.info("contributions: {}", contributions);

		return toFundingOrderDetailResponseDTO(fundingOrder, contributions);
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

	private FundingOrderDetailResponseDTO toFundingOrderDetailResponseDTO(FundingOrder fundingOrder, List<FundingContribution> contributions) {
		return FundingOrderDetailResponseDTO.builder()
			.fundingOrderId(fundingOrder.getFundingOrderId())
			.userName(fundingOrder.getUser().getUsername())
			.totalAmount(fundingOrder.getTotalAmount())
			.paymentDate(fundingOrder.getPaymentDate())
			.paymentType(fundingOrder.getPaymentType())
			.refundStatus(fundingOrder.getRefundStatus().toString())
			.address(fundingOrder.getAddress())
			.phoneNumber(fundingOrder.getPhoneNumber())
			.name(fundingOrder.getName())
			.contributions(toContributionDTOList(contributions))
			.build();
	}


	private List<FundingOrderDetailResponseDTO.FundingContributionResponseDTO> toContributionDTOList( List<FundingContribution> contributions) {
		return contributions.stream().map(this::toContributionDTO)
			.collect(Collectors.toList());
	}


	private FundingOrderDetailResponseDTO.FundingContributionResponseDTO toContributionDTO(FundingContribution contribution) {
		return FundingOrderDetailResponseDTO.FundingContributionResponseDTO.builder()
			.contributionId(contribution.getContributionId())
			.rewardId(contribution.getReward().getRewardId())
			.rewardName(contribution.getReward().getRewardName())
			.rewardPrice(contribution.getReward().getRewardPrice())
			.rewardQuantity(contribution.getRewardQuantity())
			.contributionDate(contribution.getContributionDate())
			.build();
	}






}
