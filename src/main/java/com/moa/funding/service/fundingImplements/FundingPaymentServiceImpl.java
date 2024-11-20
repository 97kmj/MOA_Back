package com.moa.funding.service.fundingImplements;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.User;
import com.moa.funding.dto.FundingOrderDTO;
import com.moa.funding.dto.PaymentRequest;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.IamPortService;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingPaymentServiceImpl implements FundingPaymentService {
	private final IamPortService iamportService;
	private final FundingOrderRepository fundingOrderRepository;
	private final FundingContributionRepository fundingContributionRepository;
	private final FundingRepository fundingRepository;
	private final UserRepository userRepository; // 주입 필요


	@Override
	@Transactional
	public FundingOrderDTO processPayment(String impUid, PaymentRequest paymentRequest) {

		// Step 1: 결제 검증
		boolean isVerified = iamportService.verifyPayment(paymentRequest.getTotalAmount(), impUid);
		if (!isVerified) {
			throw new RuntimeException("결제 검증 실패");
		}
		// Step 3: 펀딩 주문 생성 및 저장
		FundingOrder order = createFundingOrder(paymentRequest);
		FundingOrder savedOrder = fundingOrderRepository.save(order);

		// Step 4: 펀딩 후원 생성 및 저장
		Funding funding = fundingRepository.findById(paymentRequest.getFundingId())
			.orElseThrow(() -> new IllegalArgumentException("펀딩이 존재하지 않습니다."));


		FundingContribution contribution = createFundingContribution(savedOrder,funding);
		fundingContributionRepository.save(contribution);

		// Step 5: 펀딩의 currentAmount 업데이트
		updateFundingCurrentAmount(paymentRequest.getFundingId(), paymentRequest.getTotalAmount());

		return toFundingOrderDTO(savedOrder);
	}

	@Override
	public void updateFundingCurrentAmount(Long fundingId, Long amount) {
		Funding funding = fundingRepository.findById(fundingId)
		.orElseThrow(() -> new IllegalArgumentException("펀딩이 존재하지 않습니다."));
		funding.setCurrentAmount(funding.getCurrentAmount() + amount);
		fundingRepository.save(funding);
	}



	private FundingOrderDTO toFundingOrderDTO(FundingOrder savedOrder) {
		return FundingOrderDTO.builder()
			.fundingOrderId(savedOrder.getFundingOrderId())
			.totalAmount(savedOrder.getTotalAmount())
			.paymentType(savedOrder.getPaymentType())
			.build();
	}

	private FundingContribution createFundingContribution(FundingOrder savedOrder,Funding funding) {
		return FundingContribution.builder()
			.fundingOrder(savedOrder)
			.funding(funding) // Funding 객체 설정
			.rewardPrice(new BigDecimal(50000)) // 임의의 리워드 가격
			.rewardQuantity(1L)                // 임의의 리워드 수량
			.contributionDate(new Timestamp(System.currentTimeMillis())) // 현재 시간 설정
			.build();
	}

	private FundingOrder createFundingOrder(PaymentRequest paymentRequest) {
		User defaultUser = userRepository.findByUsername("user1");


		// FundingOrder 생성
		return FundingOrder.builder()
			.user(defaultUser) // 기본 사용자 설정
			.totalAmount(paymentRequest.getTotalAmount())
			.paymentType(paymentRequest.getPaymentType())
			.paymentDate(new Timestamp(System.currentTimeMillis())) // 현재 시간으로 설정
			.refundStatus(FundingOrder.RefundStatus.NOT_REFUNDED)
			.address(paymentRequest.toFundingOrder().getAddress())
			.phoneNumber(paymentRequest.toFundingOrder().getPhoneNumber())
			.name(paymentRequest.toFundingOrder().getName())
			.build();

	}
}

