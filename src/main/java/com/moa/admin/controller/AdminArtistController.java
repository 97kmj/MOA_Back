package com.moa.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.ArtistUserDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminArtistController {
	private final AdminService adminService;
	
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
	
}
