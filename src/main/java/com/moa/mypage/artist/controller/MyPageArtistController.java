package com.moa.mypage.artist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.mypage.artist.dto.RegistArtistDto;
import com.moa.mypage.artist.service.MyPageArtistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyPageArtistController {
	private final MyPageArtistService mypageArtistService;
	
	@PostMapping("/artistSubmit")
	public ResponseEntity<String> artistSubmit(
			@RequestPart("profileImage") MultipartFile profileImage,
			@RequestPart("portfolio") MultipartFile portfolio,
			@RequestPart("registArtistDto")String registArtistDto) {
		try {
			// JSON 문자열을 객체로 변환 (Jackson 사용)
			ObjectMapper objectMapper = new ObjectMapper();
	        RegistArtistDto artistDto = objectMapper.readValue(registArtistDto, RegistArtistDto.class);
			mypageArtistService.registArtist(artistDto,portfolio,profileImage);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}

}
