package com.moa.mypage.shop.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.moa.entity.Artwork.SaleStatus;
import com.moa.mypage.shop.dto.OrderArtworkDto;
import com.moa.mypage.shop.dto.OrderArtworkInfo;
import com.moa.mypage.shop.dto.SaleArtworkDto;

public interface MypageArtworkService {
	Page<SaleArtworkDto>  saleListByUser (String username, SaleStatus saleStatus,Integer page,Integer size ) throws Exception;
	Page<OrderArtworkDto> getMyOrderList (OrderArtworkInfo orderArtworkInfo) throws Exception;
}
