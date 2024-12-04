package com.moa.admin.service;

import com.moa.entity.OrderItem.ShippingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrderItemDto {
	 private Long orderItemId;
	 private Integer quantity;
	 private ShippingStatus shippingStatus;
}
