package com.moa.shop.repository;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.moa.entity.QArtwork;
import com.moa.entity.QLikeArtwork;
import com.moa.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;





@Repository
public class ArtworkDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public Artwork findLike(User username, Artwork artworkId )throws Exception {
		QLikeArtwork likeArtwork = QLikeArtwork.likeArtwork;
		return jpaQueryFactory.select(likeArtwork.artwork)
				.from(likeArtwork)
				.where(likeArtwork.user.eq(username).and(likeArtwork.artwork.eq(artworkId)))
				.fetchOne();
	}
	
}