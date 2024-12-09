package com.moa.mypage.shop.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.Order;
import com.moa.mypage.shop.dto.OrderArtworkDto;
import com.moa.mypage.shop.dto.OrderArtworkInfo;
import com.moa.mypage.shop.dto.SaleArtworkDto;
import com.moa.mypage.shop.repository.MyOrderRepository;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.OrderItemRepository;
import com.moa.repository.OrderRepository;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageArtworkServiceImpl implements MypageArtworkService {
	private final ArtworkRepository artworkRepository;
	private final OrderRepository orderRepository;
	private final MyOrderRepository myOrderRepository;

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
		Date startDate = orderArtworkInfo.getStartDate();
		Date endDate= orderArtworkInfo.getEndDate();
		Integer page= orderArtworkInfo.getPage();
		Integer size= orderArtworkInfo.getSize();
		Pageable pageable = PageRequest.of(page, size);
		System.out.println(userName);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(pageable);
		
		return null;
//		
//		return orderRepository.findOrdersWithArtworkAndPaymentDate(userName, startDate, endDate, pageable);
	}
}
