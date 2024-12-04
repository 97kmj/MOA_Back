package com.moa.shop.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.moa.entity.Order.OrderStatus;
import com.moa.entity.Order.ShippingStatus;
import com.moa.funding.dto.payment.PaymentRequest;
import com.moa.funding.dto.payment.RewardRequest;

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

	private String impUid;         // 결제 고유 ID (아임포트에서 받은 결제 고유 ID)
    private String merchant_uid;    // 주문 고유 ID
    private Long total_price;        // 결제 총 금액
    private String buyer_name;      // 구매자 이름
    private String buyer_email;     // 구매자 이메일
    private String buyer_tel;       // 구매자 전화번호
    private String buyer_addr;      // 구매자 배송지 주소
    private Long artwork_id;     // 구매한 작품의 ID (단일 상품이므로 하나만 전송)
    private Long frame_option_id;  // 선택한 프레임 옵션의 ID
    private Integer artworkStock;
    private String paymentType;
    
    
    
    public OrderDto getOrderDto() {

        return OrderDto.builder()
                .orderId(1L) // 예시 값
                .username(buyer_name)
                .totalAmount(total_price)
                .paymentDate(new Timestamp(System.currentTimeMillis()))
                .paymentType("카드")
                .status(OrderStatus.PENDING)
                .shippingStatus(ShippingStatus.NOT_SHIPPED)
                .address(buyer_addr)
                .phoneNumber(buyer_tel)
                .name(buyer_name)
                .build();
    }
}
