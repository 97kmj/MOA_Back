package com.moa.shop.dto;

import java.util.List;

import lombok.Data;

@Data
public class AddToCartRequest {
	private List<CartItemDto> itemList;
	private String username;
}
