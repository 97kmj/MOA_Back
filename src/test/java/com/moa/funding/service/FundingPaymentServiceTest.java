package com.moa.funding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.FundingOrder;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.PaymentResponseDTO;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.fundingImplements.FundingPaymentServiceImpl;
import com.moa.funding.util.MockHelper;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;

class FundingPaymentServiceTest {

	private IamPortService iamportService;
	private FundingOrderRepository fundingOrderRepository;
	private FundingContributionRepository fundingContributionRepository;
	private FundingRepository fundingRepository;
	private UserRepository userRepository;
	private RewardRepository rewardRepository;

	private FundingPaymentService paymentService;

	@BeforeEach
	void setUp() {
		iamportService = mock(IamPortService.class);
		fundingRepository = mock(FundingRepository.class);
		fundingOrderRepository = mock(FundingOrderRepository.class);
		fundingContributionRepository = mock(FundingContributionRepository.class);
		userRepository = mock(UserRepository.class);
		rewardRepository = mock(RewardRepository.class);

		paymentService = new FundingPaymentServiceImpl(
			iamportService,
			fundingOrderRepository,
			fundingContributionRepository,
			fundingRepository,
			userRepository,
			rewardRepository
		);
	}

	@AfterEach
	void tearDown() {
		reset(fundingRepository, userRepository, rewardRepository,
			fundingOrderRepository, fundingContributionRepository, iamportService);
	}

	@Test
	void testPaymentVerificationFails() {
		// Mock 결제 검증 실패
		when(iamportService.verifyPayment(50000L, "test_imp_uid")).thenReturn(false);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			50000L, "user1", 1L, List.of(MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(50000), 1L))
		);

		// 결제 검증 실패 시 RuntimeException 발생 확인
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processPayment("test_imp_uid", paymentRequest)
		);

		assertEquals("결제 검증 실패", exception.getMessage());
		verify(fundingOrderRepository, never()).save(any());
		verify(fundingContributionRepository, never()).save(any());
		verify(fundingRepository, never()).save(any());
	}


	@Test
	@DisplayName("펀딩 결제 성공 시 현재 금액 업데이트 테스트")
	void testUpdateFundingCurrentAmount() {
		// Given
		Funding mockFunding = MockHelper.createMockFunding(1L, 10000L, fundingRepository);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			5000L, "user1", 1L, List.of(MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(5000), 1L))
		);

		// When
		paymentService.updateFundingCurrentAmount(paymentRequest);

		// Then
		assertEquals(15000L, mockFunding.getCurrentAmount(), "currentAmount가 예상 값과 일치해야 합니다.");
		verify(fundingRepository).save(argThat(funding -> funding.getCurrentAmount() == 15000L));
	}

	@Test
	@DisplayName("결제 성공 시 펀딩 주문 및 후원 저장 테스트")
	void testProcessPaymentSuccess() {
		// Given
		List<RewardRequest> rewardRequests = List.of(
			MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(50000), 2L),
			MockHelper.createMockRewardRequest(2L, BigDecimal.valueOf(10000), 1L)
		);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(120000L, "user1", 1L, rewardRequests);

		User mockUser = MockHelper.createMockUser("user1", userRepository);
		Funding mockFunding = MockHelper.createMockFunding(1L, 10000L, fundingRepository);
		Reward mockReward1 = MockHelper.createMockReward(1L, rewardRepository);
		Reward mockReward2 = MockHelper.createMockReward(2L, rewardRepository);

		when(iamportService.verifyPayment(120000L, "imp_123456")).thenReturn(true);
		when(fundingOrderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(fundingContributionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		PaymentResponseDTO responseDTO = paymentService.processPayment("imp_123456", paymentRequest);

		// Then
		assertNotNull(responseDTO, "응답 DTO가 null이 아니어야 합니다.");
		assertEquals(2, responseDTO.getFundingContribution().size(), "저장된 후원의 개수는 2개여야 합니다.");
		verify(userRepository).findByUsername("user1");
		verify(fundingOrderRepository).save(any());
		verify(fundingContributionRepository, times(2)).save(any());
	}

	@Test
	@DisplayName("리워드가 없는 경우 예외 처리 테스트")
	void testProcessPaymentWithoutRewards() {
		// Given
		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			50000L, "user1", 1L, List.of() // 빈 리스트 전달
		);

		// Mock: 결제 검증 성공 설정
		when(iamportService.verifyPayment(50000L, "imp_123456")).thenReturn(true);

		// Mock: 사용자 존재 설정
		MockHelper.createMockUser("user1", userRepository);

		// Mock: 펀딩 존재 설정
		MockHelper.createMockFunding(1L, 10000L, fundingRepository);

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processPayment("imp_123456", paymentRequest)
		);

		assertEquals("리워드 정보가 존재하지 않습니다.", exception.getMessage());

		// 검증: 펀딩 주문 및 후원 저장이 호출되지 않았는지 확인
		verify(fundingOrderRepository, never()).save(any());
		verify(fundingContributionRepository, never()).save(any());
	}

}


