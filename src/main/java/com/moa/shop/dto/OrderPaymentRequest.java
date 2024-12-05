package com.moa.shop.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentRequest {

    private Long amount;        // 결제 총 금액
    private String buyerName;      // 구매자 이름
    private String buyerEmail;     // 구매자 이메일
    private String buyerTel;       // 구매자 전화번호
    private String buyerAddr;      // 구매자 배송지 주소


}


