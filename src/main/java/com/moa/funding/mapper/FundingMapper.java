package com.moa.funding.mapper;

import java.sql.Timestamp;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.funding.FundingContributionDTO;
import com.moa.funding.dto.funding.FundingOrderDTO;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.PaymentResponseDTO;

public class FundingMapper {
	private FundingMapper() {} // 인스턴스 생성 방지


	// Entity -> DTO

	public static PaymentResponseDTO toPaymentResponseDTO(FundingOrder order, FundingContribution contribution) {
		return PaymentResponseDTO.builder()
			.fundingOrder(toFundingOrderDTO(order))
			.fundingContribution(toFundingContributionDTO(contribution))
			.isPaymentVerified(true)
			.build();
	}



	// Entity -> DTO
	public static FundingOrderDTO toFundingOrderDTO(FundingOrder order) {
		return FundingOrderDTO.builder()
			.fundingOrderId(order.getFundingOrderId())
			.totalAmount(order.getTotalAmount())
			.paymentType(order.getPaymentType())
			.address(order.getAddress())
			.phoneNumber(order.getPhoneNumber())
			.name(order.getName())
			.userName(order.getUser().getUsername())
			.paymentDate(order.getPaymentDate())
			.build();
	}

	// Entity -> DTO
	public static FundingContributionDTO toFundingContributionDTO(FundingContribution contribution) {
		return FundingContributionDTO.builder()
			.contributionId(contribution.getContributionId())
			.fundingOrderId(contribution.getFundingOrder().getFundingOrderId())
			.fundingId(contribution.getFunding().getFundingId())
			.rewardId(contribution.getReward() != null ? contribution.getReward().getRewardId() : null)
			.rewardName(contribution.getReward() != null ? contribution.getReward().getRewardName() : null)
			.rewardQuantity(contribution.getRewardQuantity())
			.rewardPrice(contribution.getRewardPrice())
			.contributionDate(contribution.getContributionDate())
			.build();
	}




	// DTO -> Entity
	public static FundingOrder toFundingOrder(PaymentRequest request, User user) {
		return FundingOrder.builder()
			.user(user)
			.totalAmount(request.getTotalAmount())
			.paymentType(request.getPaymentType())
			.address(request.getAddress())
			.phoneNumber(request.getPhoneNumber())
			.name(request.getName())
			.paymentDate(new Timestamp(System.currentTimeMillis())) // 현재 시간
			.refundStatus(FundingOrder.RefundStatus.NOT_REFUNDED) // 기본값 설정
			.build();
	}

	// DTO -> Entity
	public static FundingContribution toFundingContribution(PaymentRequest request, FundingOrder order, Funding funding, Reward reward) {
		return FundingContribution.builder()
			.fundingOrder(order)
			.funding(funding)
			.reward(reward)
			.rewardPrice(request.getRewardPrice())
			.rewardQuantity(request.getRewardQuantity())
			.contributionDate(new Timestamp(System.currentTimeMillis())) // 현재 시간
			.build();
	}
}
