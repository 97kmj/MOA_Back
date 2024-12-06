package com.moa.shop.dto;

import com.moa.entity.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
	private Long saleId;
	private Long frameOptionId;
	private Integer price;
	private Integer framePrice;
	private Long cartItemId;
	private String frameOptionName;
	
	public static CartItemDto fromEntity(CartItem entity) {
		return CartItemDto.builder()
						.saleId(entity.getSale().getArtworkId())
						.frameOptionId(entity.getFrameOption()!=null ? entity.getFrameOption().getFrameOptionId() : null)
						.frameOptionName(entity.getFrameOption()!=null ? entity.getFrameOption().getFrameType() : null)
						.price(entity.getPrice())
						.framePrice(entity.getFramePrice())
						.cartItemId(entity.getCartItemId())
						.build();
	}
}
