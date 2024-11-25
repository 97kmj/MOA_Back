package com.moa.shop.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import com.moa.entity.Artwork;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.ArtworkDto;


public class ArtworkAddMapper {
	
	@Autowired
	private static UserRepository userRepository;
	
	private ArtworkAddMapper() {
		
	}
	public static Artwork toArtworkEntity(ArtworkDto artworkDto, String ArtworkImgeUrl) {
		return Artwork.builder()
			.adminCheck(artworkDto.getAdminCheck())
			.canvasType(artworkDto.getCanvasType())
			.description(artworkDto.getDescription())
			.height(artworkDto.getHeight())
			.imageUrl(ArtworkImgeUrl)
			.isStandardCanvas(artworkDto.getIsStandardCanvas())
			.length(artworkDto.getLength())
			.likeCount(artworkDto.getLikeCount())
			.price(artworkDto.getPrice())
			.saleStatus(artworkDto.getSaleStatus())
			.stock(artworkDto.getStock())
			.termsAccepted(artworkDto.getTermsAccepted())
			.title(artworkDto.getTitle())
			.width(artworkDto.getWidth())
			.artist(artworkDto.getArtistId())
			.canvas(artworkDto.getCanvasId())
			.category(artworkDto.getCategoryId())
			.subject(artworkDto.getSubjectId())
			.type(artworkDto.getTypeId())
			.build();
			
	}
}
