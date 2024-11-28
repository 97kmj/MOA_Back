package com.moa.funding.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.funding.FundingContributionDTO;
import com.moa.funding.dto.funding.FundingOrderDTO;
import com.moa.funding.dto.payment.webhook.PortOneCustomData;
import com.moa.funding.dto.payment.webhook.PortOneWebhookRequest;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.PaymentResponseDTO;
import com.moa.funding.dto.payment.RewardRequest;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FundingPaymentMapper {
	private FundingPaymentMapper() {} // 인스턴스 생성 방지


	// Entity -> DTO
	public static PaymentResponseDTO toPaymentResponseDTO(FundingOrder order, List<FundingContribution> contributions) {
		return PaymentResponseDTO.builder()
			.fundingOrder(toFundingOrderDTO(order))
			.fundingContribution(toFundingContributionDTO(contributions))
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
		FundingContributionDTO.FundingContributionDTOBuilder builder = FundingContributionDTO.builder()
			.contributionId(contribution.getContributionId())
			.fundingOrderId(contribution.getFundingOrder().getFundingOrderId())
			.fundingId(contribution.getFunding().getFundingId())
			.rewardQuantity(contribution.getRewardQuantity())
			.rewardPrice(contribution.getRewardPrice())
			.contributionDate(contribution.getContributionDate());

		// rewardId와 rewardName에 대한 조건 처리
		if (contribution.getReward() != null) {
			builder.rewardId(contribution.getReward().getRewardId());
			builder.rewardName(contribution.getReward().getRewardName());
		} else {
			builder.rewardId(null);
			builder.rewardName(null);
		}

		return builder.build();
	}

	// Entity -> DTO (리스트 FundingContribution)
	public static List<FundingContributionDTO> toFundingContributionDTO(List<FundingContribution> contributions) {
		return contributions.stream()
			.map(FundingPaymentMapper::toFundingContributionDTO)
			.collect(Collectors.toList()); // Collectors.toList()를 사용하여 리스트로 변환
	}

	// DTO -> Entity
	public static FundingOrder toFundingOrder(PaymentRequest request, User user, Funding funding) {
		return FundingOrder.builder()
			.impUid(request.getImpUid())
			.user(user)
			.funding(funding)
			.totalAmount(request.getTotalAmount())
			.paymentType(request.getPaymentType())
			.address(request.getAddress())
			.phoneNumber(request.getPhoneNumber())
			.name(request.getName())
			.paymentDate(new Timestamp(System.currentTimeMillis())) // 현재 시간
			.refundStatus(FundingOrder.RefundStatus.NOT_REFUNDED) // 기본값 설정

			.build();
	}


	// public static FundingContribution toFundingContribution(
	// 	RewardRequest rewardRequest,
	// 	FundingOrder order,
	// 	Funding funding,
	// 	Reward reward
	// ) {
	// 	return FundingContribution.builder()
	// 		.fundingOrder(order)
	// 		.funding(funding)
	// 		.reward(reward)
	// 		.rewardPrice(rewardRequest.getRewardPrice())
	// 		.rewardQuantity(rewardRequest.getRewardQuantity())
	// 		.contributionDate(new Timestamp(System.currentTimeMillis()))
	// 		.build();
	// }

	public static FundingContribution toFundingContribution(
		RewardRequest rewardRequest,
		FundingOrder order,
		Funding funding,
		Reward reward
	) {
		return FundingContribution.builder()
			.fundingOrder(order)
			.funding(funding)
			.reward(reward)
			.rewardPrice(rewardRequest.getRewardPrice()) // RewardRequest에서 가져옴
			.rewardQuantity(rewardRequest.getRewardQuantity()) // RewardRequest에서 가져옴
			.contributionDate(new Timestamp(System.currentTimeMillis()))
			.build();
	}


	public static PaymentRequest toPaymentRequest(PortOneWebhookRequest request, Payment payment) {
		String customDataJson = payment.getCustomData(); // String으로 가져오기
		PortOneCustomData customData = parseCustomData(customDataJson); // JSON 파싱
		log.info("Parsed custom_data: {}", customData);

		return PaymentRequest.builder()
			.impUid(request.getImpUid())
			.totalAmount(payment.getAmount().longValue())
			.paymentType(payment.getPayMethod())
			.fundingId(customData.getFundingId())
			.rewardList(customData.getRewardList()) // 리스트 통째로 사용
			.userName(payment.getBuyerName())
			.address(payment.getBuyerAddr())
			.phoneNumber(payment.getBuyerTel())
			.name(payment.getName())
			.build();
	}

	private static PortOneCustomData parseCustomData(String customData) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(customData, PortOneCustomData.class);
		} catch (Exception e) {
			throw new RuntimeException("Custom data 파싱 실패", e);
		}
	}

}
