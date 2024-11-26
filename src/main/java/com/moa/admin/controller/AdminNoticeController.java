package com.moa.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.RegistNoticeDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminNoticeController {
	private final AdminService adminService;
	
	@GetMapping("/adminNotice") 
	public ResponseEntity<List<NoticeDto>> getNoticeList() {
		try {
			List<NoticeDto> noticeList = adminService.allNoticeList();
			return new ResponseEntity<List<NoticeDto>>(noticeList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<NoticeDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/modifyNotice")
	public ResponseEntity<NoticeDto> modifyNotice(@RequestBody RegistNoticeDto notice) {
		try {
			NoticeDto noticeDto = adminService.modifyNotice(notice);
			return new ResponseEntity<NoticeDto>(noticeDto,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NoticeDto>(HttpStatus.BAD_REQUEST);
			
		}
	}
	
	@PostMapping("/writeNotice")
	public ResponseEntity<NoticeDto> writeNotice(@RequestBody RegistNoticeDto notice) {
		try {
			NoticeDto noticeDto = adminService.registNotice(notice);
			return new ResponseEntity<NoticeDto>(noticeDto,HttpStatus.OK);		
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<NoticeDto>(HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/deleteNotice/{noticeId}")
	public ResponseEntity<String> deleteNotice(@PathVariable Long noticeId) {
		try {
			adminService.deleteNotice(noticeId);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
}
