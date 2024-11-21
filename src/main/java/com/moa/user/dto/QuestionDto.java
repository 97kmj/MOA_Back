package com.moa.user.dto;

import java.sql.Date;

import com.moa.entity.Question;
import com.moa.repository.UserRepository;

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
	private Date questionAt;
	private Boolean answerStatus;
	
}
