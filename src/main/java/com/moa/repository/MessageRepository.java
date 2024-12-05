package com.moa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByReceiver_Username(String username);
}
