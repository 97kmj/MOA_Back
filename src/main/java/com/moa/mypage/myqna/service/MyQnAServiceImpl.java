package com.moa.mypage.myqna.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moa.mypage.myqna.dto.MyQuestionDto;
import com.moa.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyQnAServiceImpl implements MyQnAService {
	
	private final QuestionRepository questionRepository;
	//답장여부와 username으로 question조회
	@Override
	public Page<MyQuestionDto> getQuestions(String username, Boolean isAnswered, int page, int size) throws Exception {

		Pageable pageable = PageRequest.of(page,size,Sort.by("questionAt").descending());
		return questionRepository.findByUser_UsernameAndAnswerStatus(username, isAnswered, pageable)
				.map(q->MyQuestionDto.fromEntity(q));
		
	}
}
