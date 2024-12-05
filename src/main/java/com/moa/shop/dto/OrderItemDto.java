package com.moa.shop.dto;

import com.moa.entity.Artwork;
import com.moa.entity.FrameOption;

import com.moa.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
	private Long orderItemId;
	private Long orderId;
	private Long artworkId;  // 작품 ID
    private Long frameOptionId;  // 프레임 옵션 ID
    private Long price;
    private Long frameprice;
    

 
}