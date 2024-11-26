package com.moa.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.ArtistUserDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminArtistController {
	private final AdminService adminService;
	
	//신청 목록 불러오기
	@GetMapping("/adminApplyArtists")
	public ResponseEntity<List<ArtistUserDto>> getApplyArtists() {
		try {
			List<ArtistUserDto> applyArtistList = adminService.getApplyArtistList();
			return new ResponseEntity<List<ArtistUserDto>>(applyArtistList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ArtistUserDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	//작가 승인 
	@PostMapping("/approveArtist")
	public ResponseEntity<String> approveArtist(@RequestBody Map<String,String> reqBody) {
		try {
			adminService.approveArtist(reqBody.get("username"));
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	//작가 반려
	@PostMapping("/rejectArtist")
	public ResponseEntity<String> rejectArtist(@RequestBody Map<String,String> reqBody) {
		try {
			adminService.rejectArtist(reqBody.get("username"));
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
