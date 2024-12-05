package com.moa.mypage.message.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.mypage.message.dto.MessageDto;
import com.moa.mypage.message.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final MessageService messageService;
	
	@GetMapping("/message")
	public ResponseEntity<List<MessageDto>> getMessage(@RequestParam String username) {
		try {
			List<MessageDto> messageList = messageService.getMessageList(username);
			return new ResponseEntity<List<MessageDto>>(messageList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<MessageDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
