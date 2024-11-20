package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
