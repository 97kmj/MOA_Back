package com.moa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	Page<Message> findByReceiver_UsernameAndReadStatus(String username, Boolean isRead, Pageable pageable);
	Page<Message> findByReceiver_Username(String username, Pageable pageable);
	Page<Message> findBySender_Username(String username,Pageable pageable);
}
