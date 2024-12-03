package com.moa.mypage.myqna.service;

import org.springframework.data.domain.Page;

import com.moa.mypage.myqna.dto.MyQuestionDto;

public interface MyQnAService {
	Page<MyQuestionDto> getQuestions(String username, Boolean isAnswered, int page, int size) throws Exception;
	
}
