package com.moa.user.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.moa.entity.QArtwork;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MainArtworkRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	public List<Artwork> getRandomArtwork() {
		QArtwork artwork = QArtwork.artwork;
		return jpaQueryFactory.selectFrom(artwork)
				 .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
		         .limit(5)
		         .fetch();
	}
}
