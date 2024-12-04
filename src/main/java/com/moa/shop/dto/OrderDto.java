package com.moa.shop.dto;

import java.sql.Timestamp;
import java.util.List;

import com.moa.entity.Order;
import com.moa.entity.Order.OrderStatus;
import com.moa.entity.Order.ShippingStatus;
import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
	private Long orderId;
    private String username;  // 주문한 사용자
    private Long totalAmount;
    private Timestamp paymentDate;
    private String paymentType;
    private OrderStatus status;
    private ShippingStatus shippingStatus;
    private String address;
    private String phoneNumber;
    private String name;
    private List<OrderItemDto> orderItems;  // 주문 항목 추가

    public Order toOrderEntity() throws Exception {
        
        return Order.builder()
                .user(User.builder().username(username).build())
                .totalAmount(totalAmount)
                .paymentDate(paymentDate == null ? new Timestamp(System.currentTimeMillis()) : paymentDate)
                .paymentType(paymentType)
                .status(status)
                .shippingStatus(shippingStatus)
                .address(address)
                .phoneNumber(phoneNumber)
                .name(name)
                .build();
    }
    
   


}

