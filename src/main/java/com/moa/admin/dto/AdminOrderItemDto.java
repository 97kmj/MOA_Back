package com.moa.admin.dto;

import java.sql.Timestamp;

import com.moa.entity.OrderItem;
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
	 private Long orderId;
	 private Integer quantity;
	 private ShippingStatus shippingStatus;
	 private String buyerId;
	 private String sellerId;
	 private Long artworkId;
	 private String artworkTitle;
	 private String frameType;
	 private Timestamp paymentDate;
	 
	 public static AdminOrderItemDto fromEntity(OrderItem orderItem) {
		 AdminOrderItemDto dto = 
				 AdminOrderItemDto.builder()							
				 .orderItemId(orderItem.getOrderItemId())
				 .orderId(orderItem.getOrder().getOrderId())
				 .quantity(orderItem.getQuantity())
				 .shippingStatus(orderItem.getShippingStatus())
				 .buyerId(orderItem.getOrder().getUser().getUsername())
				 .sellerId(orderItem.getArtwork().getArtist().getUsername())
				 .artworkId(orderItem.getArtwork().getArtworkId())
				 .artworkTitle(orderItem.getArtwork().getTitle())
				 .frameType(orderItem.getFrameOption()!=null ? orderItem.getFrameOption().getFrameType() : null)
				 .paymentDate(orderItem.getOrder().getPaymentDate())
				 .build();
		 return dto;
	 }
	 
}
