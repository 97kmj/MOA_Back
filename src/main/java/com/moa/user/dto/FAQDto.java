package com.moa.user.dto;

import java.sql.Date;

import com.moa.entity.FAQ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FAQDto {
	private Long faqId;
	private String question;
	private String answer;
	private Date registDate;
	
	public static FAQDto fromEntity(FAQ faq) {
		FAQDto faqDto = FAQDto.builder()
				.faqId(faq.getFaqId())
				.question(faq.getQuestion())
				.answer(faq.getAnswer())
				.registDate(faq.getRegistDate())
				.build();
		return faqDto;
	}
}
