package com.moa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findByAnswerStatus(Boolean status);
	List<Question> findByAnswerStatusOrderByQuestionAtDesc(Boolean status);
	Page<Question> findByUser_UsernameAndAnswerStatus(String username, Boolean isAnswered, Pageable pageable);
}
