package com.moa.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<Map<String,Object>> getAllQuestionList() {
		try {
			Map<String,Object> param = new HashMap<>();
			List<QuestionDto> notAnswerQuestions = adminService.notAnswerQuestionList();
			List<QuestionDto> answeredQuestions = adminService.answeredQuestionList();
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
	public ResponseEntity<String> writeAnswer(@RequestParam QuestionDto questionDto) {
		try {
			
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
}
