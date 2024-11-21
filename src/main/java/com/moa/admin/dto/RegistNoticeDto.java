package com.moa.admin.dto;


import com.moa.entity.Notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistNoticeDto {
	private Long noticeId;
	private String title;
	private String content;
	
	public static Notice toEntity(RegistNoticeDto noticeDto) {
		Notice notice = Notice.builder()
				.title(noticeDto.getTitle())
				.noticeId(noticeDto.getNoticeId())
				.content(noticeDto.getContent())
				.build();
		return notice;
	}
	

}
