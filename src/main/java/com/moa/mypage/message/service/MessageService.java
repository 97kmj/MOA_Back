package com.moa.mypage.message.service;

import org.springframework.data.domain.Page;

import com.moa.mypage.message.dto.MessageDto;

public interface MessageService {
	Page<MessageDto> getMessageList(String username,String messageType, int page, int size) throws Exception;
	void sendReply(MessageDto message) throws Exception;
	void markMessageAsRead(Long messageId) throws Exception;
	
}
