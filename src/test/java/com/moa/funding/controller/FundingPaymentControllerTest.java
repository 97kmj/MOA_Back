package com.moa.funding.controller;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.funding.dto.FundingOrderDTO;
import com.moa.funding.dto.PaymentRequest;
import com.moa.funding.service.FundingPaymentService;

@WebMvcTest(FundingPaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class FundingPaymentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FundingPaymentService fundingPaymentService;


	@Test
	@DisplayName("결제 성공 시 200 응답")
	void testProcessPaymentSuccess() throws Exception {
		// Given
		String impUid = "test_imp_uid";
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTotalAmount(50000L);
		paymentRequest.setPaymentType("CARD");
		paymentRequest.setFundingId(1L);

		FundingOrderDTO mockDTO = FundingOrderDTO.builder()
			.fundingOrderId(1L)
			.totalAmount(50000L)
			.paymentType("CARD")
			.build();

		when(fundingPaymentService.processPayment(anyString(), any(PaymentRequest.class))).thenReturn(mockDTO);

		// When & Then
		mockMvc.perform(post("/api/funding/payment")
				.param("impUid", impUid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(paymentRequest))
				.with(csrf())) // CSRF 토큰 추가
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.fundingOrderId").value(1L))
			.andExpect(jsonPath("$.totalAmount").value(50000L));
	}

	@Test
	@DisplayName("결제 검증 실패 시 400 응답")
	void testProcessPaymentFailure() throws Exception {
		// Given
		String impUid = "test_imp_uid";
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setTotalAmount(50000L);
		paymentRequest.setPaymentType("CARD");
		paymentRequest.setFundingId(1L);

		when(fundingPaymentService.processPayment(anyString(), any(PaymentRequest.class)))
			.thenThrow(new RuntimeException("결제 검증 실패"));

		// When & Then
		mockMvc.perform(post("/api/funding/payment") // URL 확인
				.param("impUid", impUid)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(paymentRequest))
				.with(csrf())) // CSRF 추가
			.andExpect(status().isBadRequest()); // 400 상태 코드 검증
	}
}