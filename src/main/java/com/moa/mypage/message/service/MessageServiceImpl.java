package com.moa.mypage.message.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moa.mypage.message.dto.MessageDto;
import com.moa.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	
	@Override
	public List<MessageDto> getMessageList(String username) throws Exception {
		return messageRepository.findByReceiver_Username(username).stream().map(m->MessageDto.fromEntity(m)).collect(Collectors.toList());
	}
}
