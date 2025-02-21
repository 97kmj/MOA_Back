package com.moa.admin.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.QuestionDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminQnAController {
	private final AdminService adminService;
	
	// admin qna 로드 시 
	@GetMapping("/adminQnA") 
	public ResponseEntity<Map<String,Object>> getAllQuestionList( 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		try {
			System.out.println(startDate);
			System.out.println(endDate);
			Map<String,Object> param = new HashMap<>();
			List<QuestionDto> notAnswerQuestions = adminService.notAnswerQuestionList();
			List<QuestionDto> answeredQuestions = adminService.answeredQuestionList(Date.valueOf(startDate),Date.valueOf(endDate));
			param.put("notAnswerQuestions", notAnswerQuestions);
			param.put("answeredQuestions", answeredQuestions);
			return new ResponseEntity<Map<String,Object>>(param,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	// 답변 작성
	@PostMapping("/writeAnswer")
	public ResponseEntity<String> writeAnswer(@RequestBody QuestionDto questionDto) {
		try {
			adminService.writeAnswer(questionDto);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
}
