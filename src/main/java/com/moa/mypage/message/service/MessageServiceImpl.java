package com.moa.mypage.message.service;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.moa.entity.Message;
import com.moa.mypage.message.dto.MessageDto;
import com.moa.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	
	@Override
	public Page<MessageDto> getMessageList(String username, String messageType, int page, int size) throws Exception {
		Pageable pageable = PageRequest.of(page, size,Sort.by("createAt").descending());
		if(messageType.equals("send")) {
			return messageRepository.findBySender_Username(username, pageable).map(m->MessageDto.fromEntity(m));
		} else if (messageType.equals("receive")) {
			return messageRepository.findByReceiver_Username(username, pageable).map(m->MessageDto.fromEntity(m));
		} else if (messageType.equals("notRead")) {
			return messageRepository.findByReceiver_UsernameAndReadStatus(username, false, pageable) // 받
					.map(m->MessageDto.fromEntity(m));			
		} else {
			throw new Exception("쪽지 정보 오류");
		}	
	}
	
	@Override
	public void sendReply(MessageDto message) throws Exception {
		Long messageId = message.getMessageId();
		Message newMessage = messageRepository.findById(messageId).orElseThrow(()->new Exception("messageId 오류"));
		newMessage.setReply(message.getReply());
		newMessage.setReplyAt(new Timestamp(System.currentTimeMillis()));
		newMessage.setReplyReadState(false);
		messageRepository.save(newMessage);
	}
}
