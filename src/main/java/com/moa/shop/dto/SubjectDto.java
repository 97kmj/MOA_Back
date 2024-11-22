package com.moa.shop.dto;

import com.moa.entity.Category;
import com.moa.entity.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {
	private Integer subjectId;
	private String subjectName;
	private Integer categoryId;
	
	public static Subject toEntity(SubjectDto subjectDto) {
		Subject subject = Subject.builder()
				.subjectId(subjectDto.getSubjectId())
				.subjectName(subjectDto.getSubjectName())
				.category(Category.builder().categoryId(subjectDto.getCategoryId()).build())
				.build();
		return subject;
	}
	
	public static SubjectDto fromEntity(Subject subject) {
		SubjectDto subjectDto = SubjectDto.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.categoryId(subject.getCategory().getCategoryId())
				.build();
		return subjectDto;
	}
	
	
	
	
	
}
