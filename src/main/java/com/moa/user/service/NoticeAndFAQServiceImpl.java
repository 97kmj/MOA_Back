package com.moa.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.moa.admin.dto.NoticeDto;
import com.moa.entity.Question;
import com.moa.repository.FAQRepository;
import com.moa.repository.NoticeRepository;
import com.moa.repository.QuestionRepository;
import com.moa.repository.UserRepository;
import com.moa.user.dto.FAQDto;
import com.moa.user.dto.RequestQuestionDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeAndFAQServiceImpl implements NoticeAndFAQService {
	private final NoticeRepository noticeRepository;
	private final FAQRepository faqRepository;
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	@Override
	public List<NoticeDto> getNotices(int page, int size) throws Exception {
		List<NoticeDto> noticeList = null;
		noticeList = noticeRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).stream().map(notice->NoticeDto.fromEntity(notice)).collect(Collectors.toList());
		return noticeList;
	}
	@Override
	public Integer getNoticeCount() throws Exception {
		return (int)noticeRepository.count();
	}
	
	@Override
	public List<FAQDto> getFAQList() throws Exception {
		List<FAQDto> faqList = faqRepository.findAll().stream().map(f->FAQDto.fromEntity(f)).collect(Collectors.toList());
		return faqList;
	}
	@Override
	public void sendQuestion(RequestQuestionDto questionDto) throws Exception {
		Question question = Question.builder()
									.title(questionDto.getTitle())
									.content(questionDto.getContent())
									.user(userRepository.findById(questionDto.getUsername()).get())
									.answerStatus(false)
									.build();
		questionRepository.save(question);
	}
	

}
