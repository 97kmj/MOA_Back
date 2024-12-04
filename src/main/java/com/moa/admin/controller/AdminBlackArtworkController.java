package com.moa.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.BlackArtworkDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminBlackArtworkController {
	private final AdminService adminService;
	
	@PostMapping("/updateArtworkStatus")
	public ResponseEntity<String> updateArtworkAdminCheck(@RequestBody Map<String,Object> reqBody) {
		try {
			Long artworkId = ((Integer)reqBody.get("artworkId")).longValue();
			Boolean isSuspicious = (Boolean)reqBody.get("isSuspicious");
			adminService.changeAdminCheck(artworkId, isSuspicious);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/adminBlackArtwork")
	public ResponseEntity<List<BlackArtworkDto>> getSuspiciousArtworks() {
		try {
			List<BlackArtworkDto> artworks = adminService.getBlackArtworks();
			return new ResponseEntity<List<BlackArtworkDto>>(artworks,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<BlackArtworkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/deleteArtwork")
	public ResponseEntity<String> deleteArtwork(@RequestBody Map<String, Long> reqBody) {
		try {
			Long artworkId = reqBody.get("artworkId");
			adminService.deleteBlackArtwork(artworkId);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
}
