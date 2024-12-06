package com.moa.shop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.moa.entity.Cart;
import com.moa.entity.CartItem;
import com.moa.entity.User;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.CartItemRepository;
import com.moa.repository.CartRepository;
import com.moa.repository.FrameOptionRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.CartDto;
import com.moa.shop.dto.CartItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ArtworkRepository artworkRepository;
	private final FrameOptionRepository frameOptionRepository;
	@Transactional
	@Override
	public void addCartItems(List<CartItemDto> itemList, String username) throws Exception {
		User user = userRepository.findById(username).orElseThrow(()->new Exception("username 오류"));
		Cart cart = Cart.builder().user(user).build();
		cartRepository.save(cart);
		
		for(CartItemDto item : itemList) {
			CartItem cartItem = CartItem.builder()
										.cart(cart)
										.sale(artworkRepository.findById(item.getSaleId()).orElseThrow(()->new Exception("artworkId 오류")))
										.price(item.getPrice())
										.framePrice(item.getFramePrice())
										.quantity(1)
										.totalPrice(item.getFramePrice() + item.getPrice())
										.build();
			if(item.getFrameOptionId()!=null) {
				cartItem.setFrameOption(frameOptionRepository.findById(item.getFrameOptionId()).orElseThrow(()->new Exception("frameId오류")));
			}
			cartItemRepository.save(cartItem);
		}	
	}
	
	
	@Override
	public List<CartDto> getCart(String username) throws Exception {
		List<Cart> cartList = cartRepository.findByUser_Username(username);
		List<CartDto> cartDtoList = new ArrayList<>();
		for(Cart cart : cartList) {
			String imageUrl = cart.getCartItems().get(0).getSale().getImageUrl();
			String artworkTitle = cart.getCartItems().get(0).getSale().getTitle();
			CartDto cartDto = CartDto.builder()
									.cartId(cart.getCartId())
									.imageUrl(imageUrl)
									.artworkTitle(artworkTitle)
									.itemList(cart.getCartItems().stream().map(i->CartItemDto.fromEntity(i)).collect(Collectors.toList()))
									.build();
			cartDtoList.add(cartDto);
		}							
		return cartDtoList;
	}
	
	@Transactional
	@Override
	public void deleteCartItem(Long cartItemId) throws Exception {
		CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(()->new RuntimeException("CartItem not fount"));
		
		Cart cart = cartItem.getCart(); 
		
		// 연관된 cartItems 컬렉션에서 먼저 삭제
	    cart.getCartItems().remove(cartItem);

	    // CartItem 삭제
	    cartItemRepository.delete(cartItem);
		
		//카트 아이템 삭제후 해당 카트에 해당하는 item이 없으면 카트를 삭제
		if(cart.getCartItems().isEmpty()) {
			cartRepository.delete(cart);
		}
	}
	
	@Transactional
	@Override
	public void deleteCartList(List<Long> cartIdList) throws Exception {
		if (cartIdList == null || cartIdList.isEmpty()) {
	        throw new IllegalArgumentException("Cart ID list cannot be null or empty.");
	    }
		
		// 자식 엔티티인 CartItem 먼저 삭제
	    cartItemRepository.deleteAllByCartIds(cartIdList);
	    
		cartRepository.deleteAllByIdInBatch(cartIdList);
	}
}
