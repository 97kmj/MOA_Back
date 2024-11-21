package com.moa.user.service;

import java.util.List;

import com.moa.admin.dto.NoticeDto;
import com.moa.user.dto.FAQDto;
import com.moa.user.dto.QuestionDto;

public interface NoticeAndFAQService {
	List<NoticeDto> getNotices(int page, int size) throws Exception;
	Integer getNoticeCount() throws Exception;
	List<FAQDto> getFAQList() throws Exception;
	void sendQuestion(QuestionDto question) throws Exception;
}
