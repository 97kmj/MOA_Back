//package com.moa.funding.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import com.moa.entity.Funding;
//import com.moa.entity.FundingContribution;
//import com.moa.entity.FundingOrder;
//import com.moa.entity.Reward;
//import com.moa.entity.User;
//import com.moa.funding.dto.payment.PaymentRequest;
//import com.moa.funding.dto.payment.PaymentResponseDTO;
//import com.moa.funding.service.fundingImplements.FundingPaymentServiceImpl;
//import com.moa.funding.util.MockHelper;
//import com.moa.repository.FundingContributionRepository;
//import com.moa.repository.FundingOrderRepository;
//import com.moa.repository.FundingRepository;
//import com.moa.repository.RewardRepository;
//import com.moa.repository.UserRepository;
//
//class FundingPaymentServiceTest {
//
//	private IamPortService iamportService;
//	private FundingOrderRepository fundingOrderRepository;
//	private FundingContributionRepository fundingContributionRepository;
//	private FundingRepository fundingRepository;
//	private UserRepository userRepository;
//	private RewardRepository rewardRepository;
//
//	private FundingPaymentService paymentService;
//
//	@BeforeEach
//	void setUp() {
//		iamportService = mock(IamPortService.class);
//		fundingRepository = mock(FundingRepository.class);
//		fundingOrderRepository = mock(FundingOrderRepository.class);
//		fundingContributionRepository = mock(FundingContributionRepository.class);
//		userRepository = mock(UserRepository.class);
//		rewardRepository = mock(RewardRepository.class);
//
//		paymentService = new FundingPaymentServiceImpl(
//			iamportService,
//			fundingOrderRepository,
//			fundingContributionRepository,
//			fundingRepository,
//			userRepository,
//			rewardRepository
//		);
//	}
//
//	@AfterEach
//	void tearDown() {
//		reset(fundingRepository, userRepository, rewardRepository,
//			fundingOrderRepository, fundingContributionRepository, iamportService);
//	}
//
//	@Test
//	void testPaymentVerificationFails() {
//		// Mock 결제 검증 실패
//		// iamportService의 verifyPayment 메서드가 false를 반환하도록 설정
//		when(iamportService.verifyPayment(50000L, "test_imp_uid")).thenReturn(false);
//
//		// MockHelper를 사용하여 PaymentRequest 객체 생성
//		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(50000L, "user1", 1L, 1L);
//
//		// 결제 검증이 실패한 경우 RuntimeException이 발생해야 함
//		RuntimeException exception = assertThrows(RuntimeException.class, () ->
//			paymentService.processPayment("test_imp_uid", paymentRequest)
//		);
//
//		// 예외 메시지가 "결제 검증 실패"와 동일한지 검증
//		assertEquals("결제 검증 실패", exception.getMessage());
//
//		// 결제 검증 실패 시 관련 데이터(fundingOrder, fundingContribution, funding)가 저장되지 않았는지 검증
//		verify(fundingOrderRepository, never()).save(any());
//		verify(fundingContributionRepository, never()).save(any());
//		verify(fundingRepository, never()).save(any());
//	}
//
//	@Test
//	@DisplayName("펀딩 결제 성공 시 현재 금액 업데이트 테스트")
//	void testUpdateFundingCurrentAmount() {
//		// Given
//		// MockHelper를 사용하여 Funding 객체를 생성하고 초기화
//		Funding mockFunding = MockHelper.createMockFunding(1L, 10000L, fundingRepository);
//
//		// MockHelper를 사용하여 PaymentRequest 객체 생성
//		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(5000L, "user1", 1L, null);
//
//		// When
//		// 펀딩 금액 업데이트 메서드 호출
//		paymentService.updateFundingCurrentAmount(paymentRequest);
//
//		// Then
//		// 펀딩의 현재 금액(currentAmount)이 15000L로 업데이트되었는지 확인
//		assertEquals(15000L, mockFunding.getCurrentAmount(), "currentAmount가 예상 값과 일치해야 합니다.");
//
//		// FundingRepository의 save 메서드가 호출되었는지 확인하며, 저장된 데이터가 예상 값인지 검증
//		verify(fundingRepository).save(argThat(funding -> funding.getCurrentAmount() == 15000L));
//	}
//
//	@Test
//	@DisplayName("결제 성공 시 펀딩 주문 및 후원 저장 테스트")
//	void testProcessPaymentSuccess() {
//		// Given
//		// MockHelper를 사용하여 PaymentRequest 객체 생성
//		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(50000L, "user1", 1L, 1L);
//
//		// MockHelper를 사용하여 User, Funding, Reward, FundingOrder, FundingContribution 객체 생성
//		User mockUser = MockHelper.createMockUser("user1", userRepository);
//		Funding mockFunding = MockHelper.createMockFunding(1L, 10000L, fundingRepository);
//		Reward mockReward = MockHelper.createMockReward(1L, rewardRepository);
//		FundingOrder mockOrder = MockHelper.createMockFundingOrder(mockUser, fundingOrderRepository);
//		FundingContribution mockContribution = MockHelper.createMockFundingContribution(fundingContributionRepository);
//
//		// Mock 결제 검증 성공: iamportService의 verifyPayment 메서드가 true를 반환하도록 설정
//		when(iamportService.verifyPayment(50000L, "imp_123456")).thenReturn(true);
//
//		// When
//		// 결제 처리 메서드 호출
//		PaymentResponseDTO responseDTO = paymentService.processPayment("imp_123456", paymentRequest);
//
//		// Then
//		// 반환된 PaymentResponseDTO가 null이 아닌지 확인
//		assertNotNull(responseDTO, "응답 DTO가 null이 아니어야 합니다.");
//
//		// 관련 데이터(user, fundingOrder, fundingContribution)가 저장되었는지 검증
//		verify(userRepository).findByUsername("user1");
//		verify(fundingOrderRepository).save(any());
//		verify(fundingContributionRepository).save(any());
//	}
//
//}
//
//
