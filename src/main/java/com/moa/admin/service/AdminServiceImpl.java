package com.moa.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistNoticeDto;
import com.moa.entity.Notice;
import com.moa.repository.NoticeRepository;
import com.moa.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final NoticeRepository noticeRepository;
	private final QuestionRepository questionRepository;
	//admin notice 
	@Override
	public List<NoticeDto> allNoticeList() throws Exception {
		List<NoticeDto> noticeList = null;
		noticeList = noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(notice->NoticeDto.fromEntity(notice)).collect(Collectors.toList());
		return noticeList;
	}
	
	@Transactional
	@Override
	public NoticeDto modifyNotice(RegistNoticeDto noticeDto) throws Exception {
		Notice notice = noticeRepository.findById(noticeDto.getNoticeId()).orElseThrow(()->new Exception("공지사항 수정 오류"));
		notice.setTitle(noticeDto.getTitle());
		notice.setContent(noticeDto.getContent());
		Notice modifiedNotice = noticeRepository.save(notice);
		return NoticeDto.fromEntity(modifiedNotice);
	}
	@Transactional
	@Override
	public NoticeDto registNotice(RegistNoticeDto noticeDto) throws Exception {
		Notice notice = Notice.builder().title(noticeDto.getTitle()).content(noticeDto.getContent()).build();
		Notice newNotice = noticeRepository.save(notice);
		return NoticeDto.fromEntity(newNotice);
	}
	
	@Override
	public void deleteNotice(Long noticeId) throws Exception {
		noticeRepository.deleteById(noticeId);
	}
	
	//admin QnA
	@Override
	public List<QuestionDto> notAnswerQuestionList() throws Exception {	
		return questionRepository.findByAnswerStatus(false).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	@Override
	public List<QuestionDto> answeredQuestionList() throws Exception {
		return questionRepository.findByAnswerStatus(true).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	
	
	
	
	
	
	
}
