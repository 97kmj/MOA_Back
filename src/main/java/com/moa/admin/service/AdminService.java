package com.moa.admin.service;

import java.sql.Date;
import java.util.List;

import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistNoticeDto;

public interface AdminService {
	//관리자 공지사항
	List<NoticeDto> allNoticeList() throws Exception;
	NoticeDto modifyNotice(RegistNoticeDto noticeDto) throws Exception;
	NoticeDto registNotice(RegistNoticeDto noticeDto) throws Exception;
	void deleteNotice(Long noticeId) throws Exception;
	//관리자 1대1문의
	List<QuestionDto> notAnswerQuestionList() throws Exception;
	List<QuestionDto> answeredQuestionList(Date startDate, Date endDate) throws Exception;
	void writeAnswer(QuestionDto questionDto) throws Exception;

	
}
