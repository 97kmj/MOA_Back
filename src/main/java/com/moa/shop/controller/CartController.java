package com.moa.shop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.shop.dto.AddToCartRequest;
import com.moa.shop.dto.CartDto;
import com.moa.shop.dto.CartItemDto;
import com.moa.shop.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	
	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
		try {
			List<CartItemDto> itemList = request.getItemList();
			String username = request.getUsername();
			cartService.addCartItems(itemList, username);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/cart")
	public ResponseEntity<List<CartDto>> getCartItems(@RequestParam String username) {
		try {
			List<CartDto> cartList = cartService.getCart(username);
			return new ResponseEntity<List<CartDto>>(cartList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CartDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/deleteCartItem/{cartItemId}")
	public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
		try {
			cartService.deleteCartItem(cartItemId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/deleteCart")
	public ResponseEntity<Void> deleteCartItem(@RequestBody List<Long> cartIdList) {
		try {
			cartService.deleteCartList(cartIdList);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
