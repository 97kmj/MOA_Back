package com.moa.mypage.myqna.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.mypage.myqna.dto.MyQuestionDto;
import com.moa.mypage.myqna.service.MyQnAService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MyQnAController {
	private final MyQnAService myQnAService;
	
	@GetMapping("/myQnA")
	public ResponseEntity<Page<MyQuestionDto>> getQuestions(@RequestParam String username,
			@RequestParam Boolean isAnswered,
			@RequestParam int page,
			@RequestParam int size) {
		try {
			Page<MyQuestionDto> questionList = myQnAService.getQuestions(username, isAnswered,page,size);
			return new ResponseEntity<Page<MyQuestionDto>>(questionList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<MyQuestionDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}
