package com.moa.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.NoticeDto;
import com.moa.user.dto.FAQDto;
import com.moa.user.service.NoticeAndFAQService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoticeAndFAQController {
	private final NoticeAndFAQService noticeAndFAQService;
	
	@GetMapping("/notice")
	public ResponseEntity<Map<String,Object>> getNoticeAndFAQ(@RequestParam int page, @RequestParam int size) {
		try {
			List<NoticeDto> noticeList = noticeAndFAQService.getNotices(page,size);
			List<FAQDto> faqList = noticeAndFAQService.getFAQList();
			Integer noticeTotalCount = noticeAndFAQService.getNoticeCount();
			Map<String,Object> param = new HashMap<>();
			param.put("noticeList", noticeList);
			param.put("faqList", faqList);
			param.put("noticeTotalCount", noticeTotalCount);
			return new ResponseEntity<Map<String,Object>>(param,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
}
