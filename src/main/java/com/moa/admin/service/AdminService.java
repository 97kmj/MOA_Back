package com.moa.admin.service;

import java.util.List;

import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistNoticeDto;

public interface AdminService {
	List<NoticeDto> allNoticeList() throws Exception;
	NoticeDto modifyNotice(RegistNoticeDto noticeDto) throws Exception;
	NoticeDto registNotice(RegistNoticeDto noticeDto) throws Exception;
	void deleteNotice(Long noticeId) throws Exception;
	
	List<QuestionDto> notAnswerQuestionList() throws Exception;
	List<QuestionDto> answeredQuestionList() throws Exception;

}
