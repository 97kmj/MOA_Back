package com.moa.shop.repository;

import com.moa.entity.QLikeArtwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.moa.entity.User;
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