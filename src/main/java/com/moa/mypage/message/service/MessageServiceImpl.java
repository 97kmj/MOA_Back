package com.moa.mypage.message.service;

import org.springframework.stereotype.Service;

import com.moa.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
}
