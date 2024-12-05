package com.moa.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.service.ShopService;

@RequestMapping("/shopAdd")
@RestController
public class shopAddController {
	
	@Autowired
	private ShopService shopService;
	// 작품등록
	@PostMapping("/artworkAdd")
	public ResponseEntity<Long> artworkAdd(@RequestPart("artworkDto") ArtworkDto artworkDto,
			@RequestPart("artworkImage")MultipartFile artworkImage){
			try {
				
				
				Long artworkNum = shopService.artworkAdd(artworkDto,artworkImage);
				return new ResponseEntity<Long>(artworkNum,HttpStatus.OK);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
			}
		}
}
