package com.moa.admin.service;

import java.sql.Date;
import java.util.List;

import com.moa.admin.dto.ArtistUserDto;
import com.moa.admin.dto.FrameDto;
import com.moa.admin.dto.FundingApplyDto;
import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistFrameDto;
import com.moa.admin.dto.RegistNoticeDto;
import com.moa.shop.dto.CanvasDto;

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
	
	//관리자 작가
	List<ArtistUserDto> getApplyArtistList() throws Exception;
	void approveArtist(String username) throws Exception;
	void rejectArtist(String username) throws Exception;

	//관리자 펀딩
	List<FundingApplyDto> getApplyFundingList() throws Exception;
	void approveFunding(Long fundingId) throws Exception;
	void rejectFunding(Long fundingId) throws Exception;
	
	//관리자 프레임상품
	List<FrameDto> getFrameList() throws Exception;
	List<CanvasDto> getCanvasList(String canvasType) throws Exception;
	FrameDto registFrame(RegistFrameDto registFrameDto) throws Exception;
	void updateFrame(FrameDto frameDto) throws Exception;
}
