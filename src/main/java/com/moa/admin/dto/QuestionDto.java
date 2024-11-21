package com.moa.admin.dto;

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
public class QuestionDto {
	private Long questionId;
	private String username;
	private String title;
	private String content; 
	private Timestamp questionAt;
	private Boolean answerStatus;
	private String answerTitle;
	private String answerContent;
	private Date answerAt;
	
	public static QuestionDto fromEntity(Question question) {
		return QuestionDto.builder()
						.questionId(question.getQuestionId())
						.username(question.getUser().getUsername())
						.title(question.getTitle())
						.content(question.getContent())
						.questionAt(question.getQuestionAt())
						.answerStatus(question.getAnswerStatus())
						.answerTitle(question.getAnswerTitle())
						.answerContent(question.getAnswerContent())
						.answerAt(question.getAnswerAt())
						.build();
	}
}
