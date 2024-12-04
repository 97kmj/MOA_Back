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
	private Long artworkId;  // 작품 ID
    private Long frameOptionId;  // 프레임 옵션 ID
    private Long orderItemId;
    private Long price;
    private Long totalPrice;     // 총 금액
    private Integer quantity;  // 수량
    

    public OrderItem toOrderItemEntity() throws Exception {

    	return 	 OrderItem.builder()
    			.artwork(Artwork.builder().artworkId(artworkId).build())
    			.frameOption(FrameOption.builder().frameOptionId(frameOptionId).build())
    			.quantity(quantity)
    			.price(price)
    			.totalPrice(totalPrice)
                .build();
    }
}