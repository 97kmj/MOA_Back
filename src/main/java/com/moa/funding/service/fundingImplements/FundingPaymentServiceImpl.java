package com.moa.funding.service.fundingImplements;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.PaymentResponseDTO;
import com.moa.funding.mapper.FundingMapper;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.IamPortService;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingPaymentServiceImpl implements FundingPaymentService {
	private final IamPortService iamportService;
	private final FundingOrderRepository fundingOrderRepository;
	private final FundingContributionRepository fundingContributionRepository;
	private final FundingRepository fundingRepository;
	private final UserRepository userRepository; // 주입 필요
	private final RewardRepository rewardRepository; // 주입 필요

	@Override
	@Transactional
	public PaymentResponseDTO processPayment(String impUid, PaymentRequest paymentRequest) {

		// Step 1: 결제 검증
		boolean isVerified = iamportService.verifyPayment(paymentRequest.getTotalAmount(), impUid);
		if (!isVerified) {
			throw new RuntimeException("결제 검증 실패");
		}
		// Step 2: 사용자 조회
		User user = getUser(paymentRequest);
		// Step 3: 펀딩 주문 생성 및 저장
		FundingOrder savedOrder = createAndSaveFundingOrder(paymentRequest, user);

		// Step 4: 펀딩 조회 및 후원 생성 및 저장
		FundingContribution contribution = createAndSaveFundingContribution(paymentRequest, savedOrder);

		// Step 5: 펀딩의 currentAmount 업데이트
		updateFundingCurrentAmount(paymentRequest);

		return FundingMapper.toPaymentResponseDTO(savedOrder, contribution);
	}

	
	private FundingOrder createAndSaveFundingOrder(PaymentRequest paymentRequest, User user) {
		FundingOrder order = FundingMapper.toFundingOrder(paymentRequest, user);
		return fundingOrderRepository.save(order);
	}


	private FundingContribution createAndSaveFundingContribution(PaymentRequest paymentRequest,
		FundingOrder savedOrder) {
		Funding funding = getFunding(paymentRequest);

		Reward reward = getReward(paymentRequest);

		FundingContribution contribution = FundingMapper.toFundingContribution(paymentRequest, savedOrder, funding,
			reward);
		fundingContributionRepository.save(contribution);
		return contribution;
	}

	@Override
	public void updateFundingCurrentAmount(PaymentRequest paymentRequest) {
		Funding funding = getFunding(paymentRequest);

		funding.setCurrentAmount(funding.getCurrentAmount() + paymentRequest.getTotalAmount());
		fundingRepository.save(funding);
	}

	private Funding getFunding(PaymentRequest paymentRequest) {
		return fundingRepository.findById(paymentRequest.getFundingId())
			.orElseThrow(() -> new IllegalArgumentException("펀딩이 존재하지 않습니다."));
	}

	private Reward getReward(PaymentRequest paymentRequest) {
		return rewardRepository.findById(paymentRequest.getRewardId())
			.orElseThrow(() -> new IllegalArgumentException("리워드가 존재하지 않습니다."));
	}


	private User getUser(PaymentRequest paymentRequest) {
		return userRepository.findByUsername(paymentRequest.getUserName())
			.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
	}

}

