package com.moa.mypage.shop.service;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.OrderItem;
import com.moa.mypage.shop.dto.OrderArtworkDto;
import com.moa.mypage.shop.dto.OrderArtworkInfo;
import com.moa.mypage.shop.dto.SaleArtworkDto;
import com.moa.mypage.shop.repository.MyOrderRepository;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.OrderItemRepository;
import com.moa.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageArtworkServiceImpl implements MypageArtworkService {
	private final ArtworkRepository artworkRepository;
	private final OrderRepository orderRepository;
	private final MyOrderRepository myOrderRepository;
	private final OrderItemRepository orderItemRepository; 

	@Override
	public Page<SaleArtworkDto>  saleListByUser(String username, SaleStatus saleStatus, 
		Integer page, Integer size) throws Exception {
		Pageable pageable = PageRequest.of(page, size);
		System.out.println(saleStatus);
		System.out.println(pageable);
		if (saleStatus != null) {
			Page<SaleArtworkDto> artworkList = artworkRepository.searchByUsernameAndSaleStatus(username,saleStatus,pageable)
				.map(c->SaleArtworkDto.toArtworkDto(c));
			return artworkList;
		}else {
			Page<SaleArtworkDto>artworkList = artworkRepository.findByUsername(username,pageable)
					.map(c->SaleArtworkDto.toArtworkDto(c));
			return artworkList;	
		}
	}

	@Override
	public Page<OrderArtworkDto> getMyOrderList(OrderArtworkInfo orderArtworkInfo) throws Exception {

		String userName = orderArtworkInfo.getUserName();
		Timestamp startDate = orderArtworkInfo.getStartDate();
		Timestamp endDate= orderArtworkInfo.getEndDate();
		Integer page= orderArtworkInfo.getPage();
		Integer size= orderArtworkInfo.getSize();
		Pageable pageable = PageRequest.of(page, size);
		System.out.println(userName);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(pageable);
		
		Page<OrderItem> orderItemList = orderItemRepository.findByOrder_User_UsernameAndOrder_PaymentDateBetween(userName, startDate, endDate, pageable);
		System.out.println(orderItemList);
		
		return orderItemList.map(or->OrderArtworkDto.builder()
				.orderId(or.getOrder().getOrderId())
				.paymentDate(or.getOrder().getPaymentDate())
				.quantity(or.getQuantity())
				.artworkId(or.getArtwork().getArtworkId())
				.price(or.getPrice())
				.title(or.getArtwork().getTitle())
				.artistName(or.getArtwork().getArtist().getName())
				.imageUrl(or.getArtwork().getImageUrl()).build());
	}
}
