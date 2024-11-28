package com.moa.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.FrameDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminFrameController {
	private final AdminService adminService;
	
	@GetMapping("/adminFrame")
	public ResponseEntity<List<FrameDto>> getFrameList() {
		try {
			List<FrameDto> frameList = adminService.getFrameList();
			return new ResponseEntity<List<FrameDto>>(frameList,HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<FrameDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}
