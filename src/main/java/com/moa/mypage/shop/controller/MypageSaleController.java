package com.moa.mypage.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.entity.Artwork.SaleStatus;
import com.moa.mypage.shop.dto.OrderArtworkDto;
import com.moa.mypage.shop.dto.OrderArtworkInfo;
import com.moa.mypage.shop.dto.SaleArtworkDto;
import com.moa.mypage.shop.dto.SaleArtworkInfo;
import com.moa.mypage.shop.service.MypageArtworkService;
@RequestMapping("/mypage")
@RestController
public class MypageSaleController {
	
	@Autowired
	private MypageArtworkService mypageArtworkService;
	@PostMapping("/MaSaleList")
	public ResponseEntity<Page<SaleArtworkDto> > saleArtwork(
			@RequestBody SaleArtworkInfo artworkInfo
			){
		System.out.println(artworkInfo);
		try {
			String userName = artworkInfo.getUserName();
			SaleStatus saleStatus = artworkInfo.getSaleStatus();
			Integer page = artworkInfo.getPage();
			Integer size = artworkInfo.getSize();

			Page<SaleArtworkDto>  artworkList = mypageArtworkService.saleListByUser(userName, saleStatus, page, size);
			System.out.println(artworkList);
			return new ResponseEntity<Page<SaleArtworkDto> >(artworkList, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<SaleArtworkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/MyOrderList")
	public ResponseEntity<Page<OrderArtworkDto>> orderList(
			@RequestBody OrderArtworkInfo orderArtworkInfo
			){
		try {
			System.out.println("=============");
			System.out.println("MyOrdrerList start" + orderArtworkInfo.getStartDate());
			System.out.println("MyOrdrerList end" +orderArtworkInfo.getEndDate());
			Page<OrderArtworkDto> orderList =  mypageArtworkService.getMyOrderList(orderArtworkInfo);
			return new ResponseEntity<>(orderList,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
}
