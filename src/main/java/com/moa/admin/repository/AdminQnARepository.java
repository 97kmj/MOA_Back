package com.moa.admin.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.moa.entity.QQuestion;
import com.moa.entity.Question;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminQnARepository {
	
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<Question> findAnsweredQuestionByDate(Date startDate, Date endDate) {
		QQuestion question = QQuestion.question;
		return jpaQueryFactory.select(question)
				.from(question)
				.where(question.answerStatus.eq(true).and(question.answerAt.between(startDate, endDate)))
				.fetch();
	}
	
	
}
