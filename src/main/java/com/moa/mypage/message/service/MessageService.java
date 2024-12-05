package com.moa.mypage.message.service;

import java.util.List;

import com.moa.mypage.message.dto.MessageDto;

public interface MessageService {
	List<MessageDto> getMessageList(String username) throws Exception;
}
