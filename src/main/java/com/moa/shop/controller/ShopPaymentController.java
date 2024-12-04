
//package com.moa.shop.controller;
//
//import java.util.Map;
//

//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.moa.funding.dto.payment.PaymentRequest;
//import com.moa.shop.dto.OrderPaymentRequest;
//import com.moa.shop.service.OrderPaymentService;
//
//import io.jsonwebtoken.lang.Collections;

//
//
//@RestController
//@RequestMapping("/shop")
//public class ShopPaymentController {
//
//    @Autowired
//    private OrderPaymentService orderPaymentService;
//
//    @PostMapping("/payment")
//    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody OrderPaymentRequest orderPaymentRequest) {
//        try {
//            // 결제 처리 및 DB 저장
//        	orderPaymentService.processPayment(orderPaymentRequest);
//            return ResponseEntity.ok(Collections.singletonMap("success", true));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("success", false));
//        }
//    }
//}

