package com.moa.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.FrameDto;
import com.moa.admin.dto.RegistFrameDto;
import com.moa.admin.service.AdminService;
import com.moa.shop.dto.CanvasDto;

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
	
	@GetMapping("/getCanvas/{canvasType}")
	public ResponseEntity<List<CanvasDto>> getCanvasByCanvasType(@PathVariable String canvasType) {
		try {
			List<CanvasDto> canvasList = adminService.getCanvasList(canvasType);
			return new ResponseEntity<List<CanvasDto>>(canvasList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CanvasDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/registFrame")
	public ResponseEntity<FrameDto> registFrame(@RequestBody RegistFrameDto registFrameDto) {
		try {
			FrameDto newFrame = adminService.registFrame(registFrameDto);
			return new ResponseEntity<FrameDto>(newFrame,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<FrameDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/updateFrame")
	public ResponseEntity<String> updateFrame(@RequestBody FrameDto frameDto) {
		try {
			adminService.updateFrame(frameDto);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
}
