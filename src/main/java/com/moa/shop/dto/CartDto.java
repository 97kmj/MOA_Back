package com.moa.shop.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
	private Long cartId;
	private String imageUrl;
	private String artworkTitle;
	private List<CartItemDto> itemList;
	
}
