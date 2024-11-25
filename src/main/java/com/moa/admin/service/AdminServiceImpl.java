package com.moa.admin.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moa.admin.dto.NoticeDto;
import com.moa.admin.dto.QuestionDto;
import com.moa.admin.dto.RegistNoticeDto;
import com.moa.admin.repository.AdminQnARepository;
import com.moa.entity.Notice;
import com.moa.entity.Question;
import com.moa.repository.NoticeRepository;
import com.moa.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final NoticeRepository noticeRepository;
	private final QuestionRepository questionRepository;
	private final AdminQnARepository adminQnARepository;
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
		return questionRepository.findByAnswerStatusOrderByQuestionAtDesc(false).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	@Override
	public List<QuestionDto> answeredQuestionList(Date startDate,Date endDate) throws Exception {
		return adminQnARepository.findAnsweredQuestionByDate(startDate, endDate).stream().map(q->QuestionDto.fromEntity(q)).collect(Collectors.toList());
	}
	
	//writeAnswer
	@Override
	public void writeAnswer(QuestionDto questionDto) throws Exception {
		Question question = questionRepository.findById(questionDto.getQuestionId()).orElseThrow(()->new Exception("questionID 오류"));
		question.setAnswerAt(questionDto.getAnswerAt());
		question.setAnswerTitle(questionDto.getAnswerTitle());
		question.setAnswerContent(questionDto.getAnswerContent());
		question.setAnswerStatus(questionDto.getAnswerStatus());
		questionRepository.save(question);
	}
	
	
	
	
	
	
}
