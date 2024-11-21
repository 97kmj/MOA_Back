package com.moa.funding.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;

public class MockHelper {

	public static PaymentRequest createMockPaymentRequest(Long totalAmount, String username, Long fundingId, List<RewardRequest> rewardList) {
		return PaymentRequest.builder()
			.impUid("imp_test_123")
			.totalAmount(totalAmount)
			.fundingId(fundingId)
			.rewardList(rewardList)
			.userName(username)
			.address("서울 강남구")
			.phoneNumber("010-1234-5678")
			.name("홍길동")
			.build();
	}



	public static RewardRequest createMockRewardRequest(Long rewardId, BigDecimal rewardPrice, Long rewardQuantity) {
		return RewardRequest.builder()
			.rewardId(rewardId)
			.rewardPrice(rewardPrice)
			.rewardQuantity(rewardQuantity)
			.build();
	}


	public static User createMockUser(String username, UserRepository userRepository) {
		User user = new User();
		user.setUsername(username);
		when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
		return user;
	}

	public static Funding createMockFunding(Long fundingId, Long currentAmount, FundingRepository fundingRepository) {
		Funding funding = Funding.builder()
			.fundingId(fundingId)
			.currentAmount(currentAmount)
			.build();
		when(fundingRepository.findById(fundingId)).thenReturn(Optional.of(funding));
		return funding;
	}

	public static Reward createMockReward(Long rewardId, RewardRepository rewardRepository) {
		Reward reward = new Reward();
		reward.setRewardId(rewardId);
		when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
		return reward;
	}

	public static FundingOrder createMockFundingOrder(User user, FundingOrderRepository fundingOrderRepository) {
		FundingOrder order = new FundingOrder();
		order.setUser(user);
		when(fundingOrderRepository.save(any())).thenReturn(order);
		return order;
	}

	public static FundingContribution createMockFundingContribution(FundingContributionRepository fundingContributionRepository) {
		FundingContribution contribution = new FundingContribution();
		when(fundingContributionRepository.save(any())).thenReturn(contribution);
		return contribution;
	}
}