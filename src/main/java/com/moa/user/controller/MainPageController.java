package com.moa.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.user.service.MainPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MainPageController {
	private final MainPageService mainPageService;
	
	@GetMapping("/main")
	public ResponseEntity<Map<String,Object>> getMain() {
		try {
			Map<String,Object> response = mainPageService.getMainContents();
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
