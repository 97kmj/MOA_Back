package com.moa.shop.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.entity.OrderItem;
import com.moa.shop.dto.OrderItemDto;
import com.moa.shop.dto.OrderPaymentRequest;
import com.moa.shop.dto.PaymentRequest;
import com.moa.shop.service.OrderPaymentService;




@RestController
@RequestMapping("/shop")
public class ShopPaymentController {

    @Autowired
    private OrderPaymentService orderPaymentService;

    @PostMapping("/payment")
    public ResponseEntity<String> processPayment(
    		@RequestBody PaymentRequest  request) {
        try {
            // 결제 처리 및 DB 저장
        	OrderPaymentRequest paymentData =request.getPaymentData();
        	String username = request.getUsername();
        	List<OrderItemDto> saleData = request.getSaleData();
        	
        	orderPaymentService.processPayment(paymentData,username,saleData);
            return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
}
