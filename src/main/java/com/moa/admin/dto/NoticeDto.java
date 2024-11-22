package com.moa.admin.dto;

import java.sql.Timestamp;

import com.moa.entity.Notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
	private Long noticeId;
	private String title;
	private String content;
	private Timestamp createdAt;
	
	public static Notice toEntity(NoticeDto noticeDto) {
		Notice notice = Notice.builder()
				.createdAt(noticeDto.getCreatedAt())
				.title(noticeDto.getTitle())
				.noticeId(noticeDto.getNoticeId())
				.content(noticeDto.getContent())
				.build();
		return notice;
	}
	
	public static NoticeDto fromEntity(Notice notice) {
		NoticeDto noticeDto = NoticeDto.builder()
				.createdAt(notice.getCreatedAt())
				.noticeId(notice.getNoticeId())
				.content(notice.getContent())
				.title(notice.getTitle()).build();
		return noticeDto;
	}
}
