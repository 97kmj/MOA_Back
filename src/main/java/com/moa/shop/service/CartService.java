package com.moa.shop.service;

import java.util.List;

import com.moa.shop.dto.CartDto;
import com.moa.shop.dto.CartItemDto;

public interface CartService {
	//장바구니 담기
	void addCartItems(List<CartItemDto> itemList, String username) throws Exception;
	//장바구니 페이지 로드 시 유저의 장바구니 목록 전체 가져오기
	List<CartDto> getCart(String username) throws Exception;
	
	//장바구니 cartitem삭제
	void deleteCartItem(Long cartItemId) throws Exception;
	
	//장바구니 카트 선택한 것들 삭제 
	void deleteCartList(List<Long> cartIdList) throws Exception;
}
