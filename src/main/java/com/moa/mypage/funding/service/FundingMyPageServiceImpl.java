package com.moa.mypage.funding.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.User;
import com.moa.mypage.funding.dto.FundingContributionWithFundingDTO;
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
		log.debug("getMyContributedFunding: username: {}, status: {}, pageable: {}", username, status, pageable);

		Page<FundingOrder> fundingOrders = fundingMyPageRepository.findMyContributedFunding(username, status, pageable);

		Page<MyFundingResponseDTO> responseDTOs = fundingOrders.map(this::toMyFundingResponseDTO);

		return responseDTOs;
	}

	@Override
	public FundingOrderDetailResponseDTO getFundingOrderDetail(Long fundingOrderId) {
		log.debug("fundingOrderId: {}", fundingOrderId);
		FundingOrder fundingOrder = fundingMyPageRepository.findFundingOrderWithFunding(fundingOrderId);

		if (fundingOrder == null) {
			throw new IllegalArgumentException("해당 주문이 존재하지 않습니다.");
		}

		List<FundingContribution> contributions = fundingMyPageRepository.findFundingContributions(fundingOrderId);

		return toFundingOrderDetailResponseDTO(fundingOrder, contributions);
	}

	@Override
	public Page<MyFundingResponseDTO> getMyRegisteredFunding(String username, String status, Pageable pageable) {
		log.debug("getMyRegisteredFunding : username: {}, status: {}, pageable: {}", username, status, pageable);

		Page<Funding> funding = fundingMyPageRepository.findMyRegisteredFunding(username, status, pageable);

		return funding.map(this::toMyFundingResponseDTO);
	}


	@Override
	public Page<FundingContributionWithFundingDTO> getContributionsForMyFunding(Long fundingId, Pageable pageable) {
		log.debug("getContributionsForMyFunding : fundingId: {}, pageable: {}", fundingId, pageable);

		Page<FundingOrder> fundingOrderPage = fundingMyPageRepository.findFundingOrdersForFunding(fundingId, pageable);


		List<FundingContributionWithFundingDTO> result = fundingOrderPage.getContent().stream()
			.map(fundingOrder -> {
				List<FundingContribution> contributions = fundingMyPageRepository.findFundingContributions(fundingOrder.getFundingOrderId());
				return ToFundingContributionWithFundingDTO(fundingOrder, contributions);
			})
			.collect(Collectors.toList());


		return new PageImpl<>(result, pageable, fundingOrderPage.getTotalElements());
	}



	private MyFundingResponseDTO toMyFundingResponseDTO(Funding funding) {
		return MyFundingResponseDTO.builder()
			.fundingOrderId(null) // 내 펀딩 목록에서는 필요 없음
			.fundingId(funding.getFundingId())
			.fundingTitle(funding.getTitle())
			.fundingImage(funding.getFundingMainImageUrl())
			.fundingStatus(funding.getFundingStatus().toString())
			.goalAmount(funding.getGoalAmount())
			.startDate(funding.getStartDate())
			.endDate(funding.getEndDate())
			.build();
	}

	private MyFundingResponseDTO toMyFundingResponseDTO(FundingOrder fundingOrder) {
		return MyFundingResponseDTO.builder()
			.fundingOrderId(fundingOrder.getFundingOrderId())
			.fundingId(fundingOrder.getFunding().getFundingId())
			.fundingTitle(fundingOrder.getFunding().getTitle())
			.fundingImage(fundingOrder.getFunding().getFundingMainImageUrl())
			.fundingStatus(fundingOrder.getFunding().getFundingStatus().toString())
			.goalAmount(fundingOrder.getFunding().getGoalAmount())
			.startDate(fundingOrder.getFunding().getStartDate())
			.endDate(fundingOrder.getFunding().getEndDate())
			.build();
	}

	private FundingOrderDetailResponseDTO toFundingOrderDetailResponseDTO(FundingOrder fundingOrder,
		List<FundingContribution> contributions) {
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
			.isRefundable(isRefundable(fundingOrder))
			.contributions(toContributionDTOList(contributions))
			.build();
	}

	private List<FundingOrderDetailResponseDTO.FundingContributionResponseDTO> toContributionDTOList(
		List<FundingContribution> contributions) {
		return contributions.stream().map(this::toContributionDTO)
			.collect(Collectors.toList());
	}

	private FundingOrderDetailResponseDTO.FundingContributionResponseDTO toContributionDTO(
		FundingContribution contribution) {
		return FundingOrderDetailResponseDTO.FundingContributionResponseDTO.builder()
			.contributionId(contribution.getContributionId())
			.rewardId(contribution.getReward().getRewardId())
			.rewardName(contribution.getReward().getRewardName())
			.rewardPrice(contribution.getReward().getRewardPrice())
			.rewardQuantity(contribution.getRewardQuantity())
			.contributionDate(contribution.getContributionDate())
			.build();
	}




	private boolean isRefundable(FundingOrder fundingOrder) {
		Instant now = Instant.now().truncatedTo(ChronoUnit.DAYS);
		Funding funding = fundingOrder.getFunding();

		return funding.getApprovalStatus() == Funding.ApprovalStatus.APPROVED &&
			funding.getFundingStatus().isRefundable() &&
			funding.getEndDate().isAfter(now) &&
			fundingOrder.getRefundStatus() == FundingOrder.RefundStatus.NOT_REFUNDED &&
			fundingOrder.getTotalAmount() > 0;
	}


	public  FundingContributionWithFundingDTO ToFundingContributionWithFundingDTO(FundingOrder fundingOrder, List<FundingContribution> contributions) {
		List<FundingContributionWithFundingDTO.RewardDTO> rewards = contributions.stream()
			.map(this::ToRewardDtoOfFundingContributionWithFundingDTO)
			.collect(Collectors.toList());

		return FundingContributionWithFundingDTO.builder()
			.fundingId(fundingOrder.getFunding().getFundingId())
			.fundingOrderId(fundingOrder.getFundingOrderId())
			.refundStatus(fundingOrder.getRefundStatus().toString())
			.orderUserName(fundingOrder.getUser().getUsername())
			.totalAmount(fundingOrder.getTotalAmount())
			.paymentDate(fundingOrder.getPaymentDate())
			.paymentType(fundingOrder.getPaymentType())
			.address(fundingOrder.getAddress())
			.phoneNumber(fundingOrder.getPhoneNumber())
			.name(fundingOrder.getName())
			.rewards(rewards)
			.build();
	}

	private  FundingContributionWithFundingDTO.RewardDTO ToRewardDtoOfFundingContributionWithFundingDTO(FundingContribution contribution) {
		return FundingContributionWithFundingDTO.RewardDTO.builder()
			.rewardId(contribution.getReward().getRewardId())
			.rewardName(contribution.getReward().getRewardName())
			.rewardPrice(contribution.getReward().getRewardPrice())
			.rewardQuantity(contribution.getRewardQuantity())
			.contributionId(contribution.getContributionId())
			.contributionDate(contribution.getContributionDate())
			.build();
	}


}
