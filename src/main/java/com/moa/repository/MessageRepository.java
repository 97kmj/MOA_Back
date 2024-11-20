package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
