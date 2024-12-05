package com.moa.user.repository;

import com.moa.entity.QArtwork;
import com.moa.entity.QLikeArtist;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ArtistDetailRepository {
	private final JPAQueryFactory jpaQueryFactory;
	
	
	public List<Artwork> selectArtworksByUsernameAndType(String username, String artworkType) {
		QArtwork artwork = QArtwork.artwork;
		return jpaQueryFactory.selectFrom(artwork)
				.where(artwork.artist.username.eq(username)
						.and(artwork.saleStatus.eq(Artwork.SaleStatus.valueOf(artworkType))))
				.fetch();
	}
	
	public Boolean existsLikeArtist(String artistId, String username) {
		QLikeArtist likeArtist = QLikeArtist.likeArtist;
		return jpaQueryFactory.select(likeArtist.likeArtistId)
				.from(likeArtist)
				.where(likeArtist.artist.username.eq(artistId)
						.and(likeArtist.user.username.eq(username)))
				.fetchFirst() != null;
	}
	
	
	
	
	public Long selectArtworkCount(String username) {
		QArtwork artwork = QArtwork.artwork;
		return jpaQueryFactory.select(artwork.count())
				.from(artwork)
				.where(
						artwork.artist.username.eq(username),
						artwork.saleStatus.ne(Artwork.SaleStatus.DELETE))
				.fetchOne();
	}
	
	
	
}
