package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
