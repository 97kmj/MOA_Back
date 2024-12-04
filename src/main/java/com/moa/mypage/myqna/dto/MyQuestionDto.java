package com.moa.mypage.myqna.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.moa.entity.Question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyQuestionDto {
	private Long questionId;
	private String title;
	private String content; 
	private Timestamp questionAt;
	private Boolean answerStatus;
	private String answerTitle;
	private String answerContent;
	private Date answerAt;
	
	
	public static MyQuestionDto fromEntity(Question question) {
		return MyQuestionDto.builder()
					.questionId(question.getQuestionId())
					.title(question.getTitle())
					.content(question.getContent())
					.questionAt(question.getQuestionAt())
					.answerStatus(question.getAnswerStatus())
					.answerTitle(question.getTitle())
					.answerContent(question.getAnswerContent())
					.answerAt(question.getAnswerAt())
					.build();
	}
}
