package com.moa.funding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.funding.dto.FundingOrderDTO;
import com.moa.funding.dto.PaymentRequest;
import com.moa.funding.service.fundingImplements.FundingPaymentServiceImpl;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;

class FundingPaymentServiceTest {

	private IamPortService iamportService;
	private FundingOrderRepository fundingOrderRepository;
	private FundingContributionRepository fundingContributionRepository;
	private FundingPaymentService paymentService;
	private FundingRepository fundingRepository;

	@BeforeEach
	void setUp() {
		fundingRepository = mock(FundingRepository.class);
		fundingOrderRepository = mock(FundingOrderRepository.class);
		fundingContributionRepository = mock(FundingContributionRepository.class);
		iamportService = mock(IamPortService.class);

		paymentService = new FundingPaymentServiceImpl(
			iamportService, fundingOrderRepository, fundingContributionRepository, fundingRepository,null
		);
	}




	@Test
	void testPaymentVerificationFails() {
		// Mock 결제 검증 실패
		when(iamportService.verifyPayment(50000L, "test_imp_uid")).thenReturn(false);

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTotalAmount(50000L);

		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processPayment("test_imp_uid", paymentRequest)
		);

		assertEquals("결제 검증 실패", exception.getMessage());
		verify(fundingOrderRepository, never()).save(any());
		verify(fundingContributionRepository, never()).save(any());
	}

	@Test
	void testPaymentVerificationSuccessAndDataSave() {
		// Given
		Funding mockFunding = Funding.builder()
			.fundingId(1L)
			.currentAmount(10000L) // 초기 금액 설정
			.build();

		FundingOrder mockOrder = FundingOrder.builder()
			.fundingOrderId(1L)
			.totalAmount(50000L)
			.paymentType("CARD")
			.build();

		FundingContribution mockContribution = FundingContribution.builder()
			.contributionId(1L)
			.fundingOrder(mockOrder)
			.funding(mockFunding)
			.rewardPrice(new BigDecimal(50000))
			.rewardQuantity(1L)
			.build();

		// Mock 설정
		when(iamportService.verifyPayment(50000L, "test_imp_uid")).thenReturn(true);
		when(fundingRepository.findById(1L)).thenReturn(Optional.of(mockFunding));
		when(fundingOrderRepository.save(any(FundingOrder.class))).thenReturn(mockOrder);
		when(fundingContributionRepository.save(any(FundingContribution.class))).thenReturn(mockContribution);

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTotalAmount(50000L);
		paymentRequest.setPaymentType("CARD");
		paymentRequest.setFundingId(1L);

		// When
		FundingOrderDTO result = paymentService.processPayment("test_imp_uid", paymentRequest);

		// Then
		assertNotNull(result, "결과 DTO는 null이 아니어야 합니다.");
		assertEquals(1L, result.getFundingOrderId()); // ID 검증
		assertEquals(50000L, result.getTotalAmount()); // 총 금액 검증
		assertEquals("CARD", result.getPaymentType()); // 결제 타입 검증

		// currentAmount 업데이트 확인
		assertEquals(60000L, mockFunding.getCurrentAmount(), "currentAmount 업데이트가 예상대로 이루어져야 합니다.");

		// 저장 메서드 호출 검증
		verify(fundingRepository, times(1)).findById(1L);
		verify(fundingRepository, times(1)).save(any(Funding.class));
		verify(fundingOrderRepository, times(1)).save(any(FundingOrder.class));
		verify(fundingContributionRepository, times(1)).save(any(FundingContribution.class));
	}

	@Test
	@DisplayName("펀딩 결제 성공 시 현재 금액 업데이트 테스트")
	void testCurrentAmountUpdate() {
		//given
		Funding mockFunding = Funding.builder()
			.fundingId(1L)
			.currentAmount(10000L).build();

		when(fundingRepository.findById(1L)).thenReturn(Optional.of(mockFunding));
		//when
		paymentService.updateFundingCurrentAmount(1L, 5000L); // 5000 추가
		// Then
		assertEquals(15000L, mockFunding.getCurrentAmount(), "currentAmount가 예상 값과 일치해야 합니다.");
		verify(fundingRepository).save(argThat(funding -> funding.getCurrentAmount() == 15000L));
	}

	@Test
	@DisplayName("펀딩 결제시 펀딩 주문 및 기부 내역 저장 테스트")
	void testSaveFundingOrderAndContribution() {
	//Given
		FundingOrder order = FundingOrder.builder()
			.totalAmount(50000L)
			.paymentType("CARD")
			.build();

		Funding funding = new Funding();
		funding.setFundingId(1L);
		funding.setCurrentAmount(10000L);

		FundingContribution contribution = FundingContribution.builder()
			.fundingOrder(order)
			.funding(funding)
			.rewardPrice(new BigDecimal(50000))
			.rewardQuantity(1L)
			.build();


		when(fundingRepository.findById(1L)).thenReturn(Optional.of(funding));// 펀딩 조회후
		when(fundingOrderRepository.save(any(FundingOrder.class))).thenReturn(order); // 펀딩 주문 저장
		when(fundingContributionRepository.save(any(FundingContribution.class))).thenReturn(contribution); // 기부 내역 저장
		when(iamportService.verifyPayment(anyLong(), anyString())).thenReturn(true);

		PaymentRequest mockRequest = mockPaymentRequest();

		// When
		FundingOrderDTO result = paymentService.processPayment("imp_uid", mockRequest);

		// Then
		assertNotNull(result, "결과 DTO는 null이 아니어야 합니다.");
		verify(fundingOrderRepository).save(any(FundingOrder.class));
		verify(fundingContributionRepository).save(any(FundingContribution.class));
		assertEquals(15000L, funding.getCurrentAmount()); // currentAmount 업데이트 확인


	}

	private PaymentRequest mockPaymentRequest() {
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTotalAmount(5000L); // 총 결제 금액
		paymentRequest.setPaymentType("CARD"); // 결제 유형
		paymentRequest.setFundingId(1L); // 결제 대상 펀딩 ID
		return paymentRequest;
	}

	}



