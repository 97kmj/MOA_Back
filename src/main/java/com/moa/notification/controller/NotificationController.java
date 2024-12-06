package com.moa.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.moa.notification.service.sseService.SseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
	private  final SseService sseService;


	@GetMapping("/subscribe/{username}")
	public SseEmitter subscribe(@PathVariable String username) {
		return sseService.subscribe(username);
	}

}
