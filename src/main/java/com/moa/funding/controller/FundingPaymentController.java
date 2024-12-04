package com.moa.funding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.config.auth.PrincipalDetails;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.service.FundingPaymentService;
import com.moa.funding.service.FundingRefundService;
import com.moa.funding.service.portone.PortOneService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/funding")
@RequiredArgsConstructor
public class FundingPaymentController {

	private final FundingPaymentService fundingPaymentService;
	private final PortOneService portOneService;
	private final FundingRefundService fundingRefundService;

	@PostMapping("/payment/prepare")
	public ResponseEntity<String> preparePayment(@RequestBody PaymentRequest paymentRequest,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		String userName = principalDetails.getUser().getUsername();
		//portOne 에 사전 결제 등록 요청
		boolean success = portOneService.preparePayment(paymentRequest.getMerchantUid(), paymentRequest.getAmount());

		if (!success) {
			throw new RuntimeException("결제 준비 실패"); // 전역 핸들러로 위임
		}
		fundingPaymentService.prepareFundingOrder(paymentRequest, userName);
		return ResponseEntity.ok("결제 준비 완료");
	}

	@PostMapping("/payment/complete")
	public ResponseEntity<Void> processPayment(@RequestBody PaymentRequest paymentRequest) {
		log.info("impUid: {}, paymentRequest: {}", paymentRequest.getImpUid(), paymentRequest);
		fundingPaymentService.completeFundingContribution(paymentRequest.getImpUid(), paymentRequest);
		return ResponseEntity.ok().build();
	}

	//단순 변심
	@PostMapping("/refund/individual/{fundingOrderId}")
	public ResponseEntity<String> refundIndividualFunding(@PathVariable Long fundingOrderId,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {

		if (principalDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 정보가 없습니다.");
		}

		String username = principalDetails.getUser().getUsername();
		log.info("Refund : {}", username);

		fundingRefundService.refundIndividualFunding(fundingOrderId, username);
		return ResponseEntity.ok("환불 요청이 처리되었습니다.");
	}

	// @PostMapping("/payment/prepare")
	// public ResponseEntity<String> preparePayment(@RequestBody PaymentRequest paymentRequest) {
	// 	boolean success = portOneService.preparePayment(paymentRequest.getMerchantUid(), paymentRequest.getAmount());
	//
	// 	if (!success) {
	// 		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 준비 실패");
	// 	}
	// 	fundingPaymentService.prepareFundingOrder(paymentRequest);
	//
	// 	return ResponseEntity.ok("결제 준비 완료");
	// }

	//
	// @PostMapping("/payment")
	// public ResponseEntity<Void> processPayment(@RequestBody PaymentRequest paymentRequest) {
	// 	log.info("impUid: {}, paymentRequest: {}", paymentRequest.getImpUid(), paymentRequest);
	// 	try {
	// 		fundingPaymentService.processFundingContribution(paymentRequest.getImpUid(), paymentRequest);
	// 		return ResponseEntity.ok().build();
	// 	} catch (RuntimeException e) {
	// 		log.error("결제 처리 중 오류 발생: {}", e.getMessage(), e);
	// 		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	// 	}
	// }

	// //단순 변심
	// @PostMapping("/refund/individual/{fundingOrderId}")
	// public ResponseEntity<String> refundIndividualFunding(@PathVariable Long fundingOrderId) {
	// 	try {
	// 		fundingRefundService.refundIndividualFunding(fundingOrderId);
	// 		return ResponseEntity.ok("환불 요청이 처리되었습니다.");
	// 	} catch (Exception e) {
	// 		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	// 	}
	// }

}

