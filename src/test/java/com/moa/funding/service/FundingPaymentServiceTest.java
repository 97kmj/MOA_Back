

package com.moa.funding.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

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
import com.moa.funding.dto.payment.RewardRequest;
import com.moa.funding.repository.FundingManagementRepositoryCustom;
import com.moa.funding.repository.FundingSelectRepositoryCustom;
import com.moa.funding.service.implement.FundingPaymentServiceImpl;
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
	private FundingSelectRepositoryCustom fundingSelectRepositoryCustom;
	private RewardStockCache rewardStockCache;
	private FundingManagementRepositoryCustom fundingManagementRepositoryCustom;

	@BeforeEach
	void setUp() {
		iamportOneService = mock(PortOneService.class);
		fundingRepository = mock(FundingRepository.class);
		fundingOrderRepository = mock(FundingOrderRepository.class);
		fundingContributionRepository = mock(FundingContributionRepository.class);
		userRepository = mock(UserRepository.class);
		rewardRepository = mock(RewardRepository.class);
		rewardService = mock(RewardService.class);
		fundingSelectRepositoryCustom = mock(FundingSelectRepositoryCustom.class);
		rewardStockCache = mock(RewardStockCache.class);
		fundingManagementRepositoryCustom = mock(FundingManagementRepositoryCustom.class);


		paymentService = new FundingPaymentServiceImpl(
			iamportOneService,
			fundingOrderRepository,
			fundingContributionRepository,
			fundingRepository,
			userRepository,
			rewardRepository,
			rewardService,
			fundingSelectRepositoryCustom,
			rewardStockCache,
			fundingManagementRepositoryCustom


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
			paymentService.completeFundingContribution("test_imp_uid", paymentRequest)
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
		Funding mockFunding = MockHelper.createMockFunding(1L, BigDecimal.valueOf(10000L), fundingRepository);

		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			5000L, "user1", 1L, List.of(MockHelper.createMockRewardRequest(1L, BigDecimal.valueOf(5000), 1L))
		);

		// When
		paymentService.updateFundingCurrentAmount(paymentRequest);

		// Then
		assertEquals(BigDecimal.valueOf(15000), mockFunding.getCurrentAmount(), "currentAmount가 예상 값과 일치해야 합니다.");
		verify(fundingRepository).save(argThat(funding ->
			funding.getCurrentAmount().compareTo(BigDecimal.valueOf(15000)) == 0
		));
	}
	@Test
	@DisplayName("펀딩 주문 준비 테스트 - 리워드 재고 감소 및 주문 저장")
	void testPrepareFundingOrder() {
		// Given
		List<RewardRequest> rewardRequests = List.of(
			RewardRequest.builder()
				.rewardId(1L)
				.rewardPrice(BigDecimal.valueOf(50000))
				.rewardQuantity(2L)
				.build(),
			RewardRequest.builder()
				.rewardId(2L)
				.rewardPrice(BigDecimal.valueOf(10000))
				.rewardQuantity(1L)
				.build()
		);

		PaymentRequest paymentRequest = PaymentRequest.builder()
			.fundingId(1L)
			.userName("user1")
			.rewardList(rewardRequests)
			.build();

		Funding funding = Funding.builder()
			.fundingId(1L)
			.title("펀딩 제목")
			.currentAmount(BigDecimal.ZERO)
			.goalAmount(BigDecimal.valueOf(100000))
			.startDate(Timestamp.valueOf("2024-12-01 00:00:00").toInstant())
			.endDate(Timestamp.valueOf("2025-12-31 23:59:59").toInstant())
			.build();

		User user = User.builder()
			.username("user1")
			.name("홍길동")
			.build();

		FundingOrder fundingOrder = FundingOrder.builder()
			.fundingOrderId(1L)
			.user(user)
			.funding(funding)
			.build();

		// Mock Reward 설정
		Reward reward1 = Reward.builder()
			.rewardId(1L)
			.rewardPrice(BigDecimal.valueOf(50000))
			.build();

		Reward reward2 = Reward.builder()
			.rewardId(2L)
			.rewardPrice(BigDecimal.valueOf(10000))
			.build();

		// Mock 설정
		when(fundingRepository.findById(1L)).thenReturn(Optional.of(funding));
		when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
		when(fundingOrderRepository.save(any())).thenReturn(fundingOrder);
		when(rewardService.getReward(rewardRequests.get(0))).thenReturn(reward1);
		when(rewardService.getReward(rewardRequests.get(1))).thenReturn(reward2);
		when(rewardStockCache.incrementAndCheckLimit("user1", 1L)).thenReturn(true); // 첫 번째 리워드
		when(rewardStockCache.incrementAndCheckLimit("user1", 2L)).thenReturn(true); // 두 번째 리워드

		// When
		paymentService.prepareFundingOrder(paymentRequest, "user1");

		// Then
		verify(rewardService, times(2)).reduceRewardStock(any(RewardRequest.class));
		verify(fundingOrderRepository).save(any(FundingOrder.class));
		verify(rewardStockCache).addRewardInfo(eq("merchantUid"), eq(rewardRequests));
	}


	@Test
	@DisplayName("결제 후 펀딩 후원 처리 테스트 - 정상 흐름")
	void testCompleteFundingContribution() {
		// Given
		String impUid = "imp_test_123";
		List<RewardRequest> rewardRequests = List.of(
			RewardRequest.builder()
				.rewardId(1L)
				.rewardPrice(BigDecimal.valueOf(50000))
				.rewardQuantity(2L)
				.build(),
			RewardRequest.builder()
				.rewardId(2L)
				.rewardPrice(BigDecimal.valueOf(10000))
				.rewardQuantity(1L)
				.build()
		);

		PaymentRequest paymentRequest = PaymentRequest.builder()
			.fundingId(1L)
			.userName("user1")
			.rewardList(rewardRequests)
			.totalAmount(120000L)
			.merchantUid("merchant_test_123")
			.build();

		Funding funding = Funding.builder()
			.fundingId(1L)
			.title("펀딩 제목")
			.currentAmount(BigDecimal.ZERO)
			.goalAmount(BigDecimal.valueOf(100000))
			.startDate(Timestamp.valueOf("2024-12-01 00:00:00").toInstant())
			.endDate(Timestamp.valueOf("2024-12-31 23:59:59").toInstant())
			.build();

		FundingOrder fundingOrder = FundingOrder.builder()
			.fundingOrderId(1L)
			.funding(funding)
			.user(User.builder().username("user1").name("홍길동").build())
			.build();

		Reward reward1 = Reward.builder()
			.rewardId(1L)
			.rewardPrice(BigDecimal.valueOf(50000))
			.build();

		Reward reward2 = Reward.builder()
			.rewardId(2L)
			.rewardPrice(BigDecimal.valueOf(10000))
			.build();

		// Mock 설정
		when(iamportOneService.verifyPayment(120000L, impUid)).thenReturn(true);
		when(fundingOrderRepository.existsByImpUid(impUid)).thenReturn(false);
		when(fundingOrderRepository.findByMerchantUid("merchant_test_123")).thenReturn(Optional.of(fundingOrder));
		when(fundingOrderRepository.save(any(FundingOrder.class))).thenReturn(fundingOrder);
		when(fundingRepository.findById(1L)).thenReturn(Optional.of(funding));
		when(rewardService.getReward(rewardRequests.get(0))).thenReturn(reward1);
		when(rewardService.getReward(rewardRequests.get(1))).thenReturn(reward2);

		// When
		paymentService.completeFundingContribution(impUid, paymentRequest);

		// Then
		verify(iamportOneService).verifyPayment(120000L, impUid); // 결제 검증 호출 확인
		verify(fundingOrderRepository).findByMerchantUid("merchant_test_123"); // 주문 조회 호출 확인
		verify(fundingOrderRepository).save(any(FundingOrder.class)); // 주문 저장 호출 확인
		verify(fundingContributionRepository, times(2)).save(any(FundingContribution.class)); // 후원 저장 호출 확인
		verify(fundingRepository).save(any(Funding.class)); // 펀딩 금액 업데이트 확인
	}


	@Test
	@DisplayName("리워드가 없는 경우 예외 처리 테스트")
	void testCompleteFundingContributionWithoutRewards() {
		// Given
		PaymentRequest paymentRequest = MockHelper.createMockPaymentRequest(
			50000L, "user1", 1L, List.of() // 빈 리스트 전달
		);

		// Mock: 결제 검증 성공 설정
		when(iamportOneService.verifyPayment(50000L, "imp_123456")).thenReturn(true);

		// Mock: 사용자 존재 설정
		MockHelper.createMockUser("user1", userRepository);

		// Mock: 펀딩 존재 설정
		MockHelper.createMockFunding(1L, BigDecimal.valueOf(10000L), fundingRepository);

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class, () ->
			paymentService.completeFundingContribution("imp_123456", paymentRequest)
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
			paymentService.completeFundingContribution("imp_123456", paymentRequest)
		);
		assertEquals("결제 검증 실패", exception.getMessage());
	}







}


