package com.moa.mypage.message.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<Page<MessageDto>> getMessage(@RequestParam String username,
			@RequestParam String messageType,
			@RequestParam int page,
			@RequestParam int size) {
		try {
			Page<MessageDto> messageList = messageService.getMessageList(username, messageType, page, size);
			return new ResponseEntity<Page<MessageDto>>(messageList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Page<MessageDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/replyMessage")
	public ResponseEntity<String> getMessage(@RequestBody MessageDto message) {
		try {
			messageService.sendReply(message);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PatchMapping("/message/{messageId}/read")
	public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
		try {
			messageService.markMessageAsRead(messageId);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
