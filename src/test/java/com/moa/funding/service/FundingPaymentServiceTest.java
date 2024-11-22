

package com.moa.funding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.moa.entity.Funding;
import com.moa.entity.FundingContribution;
import com.moa.entity.Reward;
import com.moa.entity.User;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.service.fundingImplements.FundingPaymentServiceImpl;
import com.moa.funding.service.portone.PortOneService;
import com.moa.funding.util.MockHelper;
import com.moa.repository.FundingContributionRepository;
import com.moa.repository.FundingOrderRepository;
import com.moa.repository.FundingRepository;
import com.moa.repository.RewardRepository;
import com.moa.repository.UserRepository;

class FundingPaymentServiceTest {

	private PortOneService iamportOneService;
	private FundingOrderRepository fundingOrderRepository;
	private FundingContributionRepository fundingContributionRepository;
	private FundingRepository fundingRepository;
	private UserRepository userRepository;
	private RewardRepository rewardRepository;

	private FundingPaymentService paymentService;
	private RewardService rewardService;

	@BeforeEach
	void setUp() {
		iamportOneService = mock(PortOneService.class);
		fundingRepository = mock(FundingRepository.class);
		fundingOrderRepository = mock(FundingOrderRepository.class);
		fundingContributionRepository = mock(FundingContributionRepository.class);
		userRepository = mock(UserRepository.class);
		rewardRepository = mock(RewardRepository.class);
		rewardService = mock(RewardService.class);

		paymentService = new FundingPaymentServiceImpl(
			iamportOneService,
			fundingOrderRepository,
			fundingContributionRepository,
			fundingRepository,
			userRepository,
			rewardRepository,
			rewardService

		);
	}

	@AfterEach
	void tearDown() {
		reset(fundingRepository, userRepository, rewardRepository,
			fundingOrderRepository, fundingContributionRepository, iamportOneService);
	}

	@Test
	void testPaymentVerificationFails() {
		// Mock 결제 검증 실패
		when(iamportOneService.verifyPayment(50000L, "test_imp_uid")).thenReturn(false);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			50000L, "user1", 1L, List.of(MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(50000), 1L))
		);

		// 결제 검증 실패 시 RuntimeException 발생 확인
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processFundingContribution("test_imp_uid", paymentRequest)
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
	@DisplayName("결제 성공 시 펀딩 주문 및 후원 저장 테스트") // 테스트의 목적: 결제 성공 시 주문 및 후원이 올바르게 저장되는지 확인
	void testProcessFundingContributionSuccess() {
		// Given: 테스트에 필요한 Mock 데이터 및 의존성 설정
		// 사용자가 선택한 리워드 요청 목록 생성
		List<RewardRequest> rewardRequests = List.of(
			MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(50000), 2L), // 리워드 ID 1, 가격 50,000원, 수량 2개
			MockHelper.createMockRewardRequest(2L, BigDecimal.valueOf(10000), 1L)  // 리워드 ID 2, 가격 10,000원, 수량 1개
		);

		// 결제 요청 객체 생성
		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(120000L, "user1", 1L, rewardRequests);

		// Mock 데이터를 통해 사용자, 펀딩, 리워드 객체를 생성하고 Mock 리포지토리에 등록
		User mockUser = MockHelper.createMockUser("user1", userRepository); // 사용자 데이터
		Funding mockFunding = MockHelper.createMockFunding(1L, 10000L, fundingRepository); // 펀딩 데이터
		Reward mockReward1 = MockHelper.createMockReward(1L, rewardRepository); // 리워드 1 데이터
		Reward mockReward2 = MockHelper.createMockReward(2L, rewardRepository); // 리워드 2 데이터

		// Mock 설정: 결제 검증 서비스가 항상 성공하도록 설정
		when(iamportOneService.verifyPayment(120000L, "imp_123456")).thenReturn(true);

		when(rewardService.getReward(any())).thenAnswer(invocation -> {
			RewardRequest request = invocation.getArgument(0);
			if (request == null) {
				return null; // null 처리
			}
			return request.getRewardId().equals(1L) ? mockReward1 : mockReward2;
		});

		// When: 테스트 대상 메서드 호출
		// 결제를 처리하는 메서드 호출 (실제 로직 수행)
		paymentService.processFundingContribution("imp_123456", paymentRequest);

		// Then: 결과 검증
		// Mock 리포지토리 호출 여부를 확인
		verify(userRepository).findByUsername("user1"); // 사용자 조회가 한 번 호출되었는지 확인
		verify(fundingOrderRepository).save(any());     // 펀딩 주문이 저장되었는지 확인

		// ArgumentCaptor를 사용해 저장된 후원 객체를 캡처하고 검증
		ArgumentCaptor<FundingContribution> contributionCaptor = ArgumentCaptor.forClass(FundingContribution.class);
		verify(fundingContributionRepository, times(2)).save(contributionCaptor.capture()); // 두 번 저장되었는지 확인

		// 저장된 후원 객체 리스트 가져오기
		List<FundingContribution> savedContributions = contributionCaptor.getAllValues();

		// 저장된 후원의 개수 확인
		assertEquals(2, savedContributions.size(), "저장된 후원의 개수는 2개여야 합니다.");

		// 각 후원의 Reward 필드가 null이 아닌지 확인
		assertNotNull(savedContributions.get(0).getReward(), "첫 번째 후원의 Reward는 null이 아니어야 합니다.");
		assertNotNull(savedContributions.get(1).getReward(), "두 번째 후원의 Reward는 null이 아니어야 합니다.");

		// 저장된 각 후원의 Reward가 예상된 리워드와 일치하는지 확인
		assertEquals(mockReward1.getRewardId(), savedContributions.get(0).getReward().getRewardId());
		assertEquals(mockReward2.getRewardId(), savedContributions.get(1).getReward().getRewardId());
	}


	@Test
	@DisplayName("리워드가 없는 경우 예외 처리 테스트")
	void testProcessFundingContributionWithoutRewards() {
		// Given
		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			50000L, "user1", 1L, List.of() // 빈 리스트 전달
		);

		// Mock: 결제 검증 성공 설정
		when(iamportOneService.verifyPayment(50000L, "imp_123456")).thenReturn(true);

		// Mock: 사용자 존재 설정
		MockHelper.createMockUser("user1", userRepository);

		// Mock: 펀딩 존재 설정
		MockHelper.createMockFunding(1L, 10000L, fundingRepository);

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processFundingContribution("imp_123456", paymentRequest)
		);

		assertEquals("리워드 정보가 존재하지 않습니다.", exception.getMessage());

		// 검증: 펀딩 주문 및 후원 저장이 호출되지 않았는지 확인
		verify(fundingOrderRepository, never()).save(any());
		verify(fundingContributionRepository, never()).save(any());
	}



	@Test
	@DisplayName("결제 검증 실패 시 RuntimeException 발생")
	void testVerifyPaymentFails() {
		// Given
		when(iamportOneService.verifyPayment(120000L, "imp_123456")).thenReturn(false);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(120000L, "user1", 1L, List.of());

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.processFundingContribution("imp_123456", paymentRequest)
		);
		assertEquals("결제 검증 실패", exception.getMessage());
	}







}


