package com.moa.user.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.moa.entity.Artwork;
import com.moa.entity.QArtwork;
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
	
//	public List<Artwork> selectNotSaleArtworkByUsername(String username) {
//		QArtwork artwork = QArtwork.artwork;
//		return jpaQueryFactory.selectFrom(artwork)
//				.where(artwork.artist.username.eq(username)
//						.and(artwork.saleStatus.eq(Artwork.SaleStatus.NOT_SALE)))
//				.fetch();
//	}
//	
//	public List<Artwork> selectSoldOutArtworkByUsername(String username) {
//		QArtwork artwork = QArtwork.artwork;
//		return jpaQueryFactory.selectFrom(artwork)
//				.where(artwork.artist.username.eq(username)
//						.and(artwork.saleStatus.eq(Artwork.SaleStatus.SOLD_OUT)))
//				.fetch();
//	}
//	
//	public List<Artwork> selectSaleArtworkByUsername(String username) {
//		QArtwork artwork = QArtwork.artwork;
//		return jpaQueryFactory.selectFrom(artwork)
//				.where(artwork.artist.username.eq(username)
//						.and(artwork.saleStatus.eq(Artwork.SaleStatus.AVAILABLE)))
//				.fetch();
//	}
	
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
