package com.moa.mypage.artist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.mypage.artist.dto.EditArtistDto;
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
	
	@GetMapping("/artistInfo")
	public ResponseEntity<EditArtistDto> getArtistInfo(@RequestParam String username) {
		try {
			EditArtistDto artistInfo = mypageArtistService.getArtistInfo(username);
			return new ResponseEntity<EditArtistDto>(artistInfo,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<EditArtistDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/editArtist")
	public ResponseEntity<String> editArtist(
			@RequestPart(value="profileImage", required = false) MultipartFile profileImage,
			@RequestPart("editArtistDto")String editArtistDto) {
		try {
			// JSON 문자열을 객체로 변환 (Jackson 사용)
			ObjectMapper objectMapper = new ObjectMapper();
	        EditArtistDto artistDto = objectMapper.readValue(editArtistDto, EditArtistDto.class);
			mypageArtistService.modifyArtistInfo(artistDto,profileImage);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}

}
