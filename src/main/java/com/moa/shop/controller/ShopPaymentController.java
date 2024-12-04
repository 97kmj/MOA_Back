package com.moa.shop.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.shop.dto.OrderPaymentRequest;

import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
@RequestMapping("/shop")

@RestController
@RequiredArgsConstructor
public class ShopPaymentController {
	
	// private final OrderPaymentService orderPaymentService;
	
	
	
	
	 @PostMapping("/payment")
	    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody OrderPaymentRequest orderPaymentRequest) {
	     
		return null;
	    }
	
}
