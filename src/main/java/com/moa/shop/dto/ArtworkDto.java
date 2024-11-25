package com.moa.shop.dto;


import com.moa.entity.Artwork;
import com.moa.entity.Artwork.CanvasType;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.Canvas;
import com.moa.entity.Category;
import com.moa.entity.Subject;
import com.moa.entity.Type;
import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtworkDto {
	private long artworkId;
	private Boolean adminCheck;
	private CanvasType canvasType;
	private String description;
	private String height;
	private String imageUrl;
	private Boolean isStandardCanvas;
	private String length;
	private Integer likeCount;
	private Long price;
	private SaleStatus saleStatus;
	private Integer stock;
	private Boolean termsAccepted;
	private String title;
	private String width;
	private User artistId;
	private Canvas canvasId;
	private Category categoryId;
	private Subject subjectId;
	private Type typeId;
	
	public static Artwork toEntity(ArtworkDto artworkDto) {
		Artwork artwork = Artwork.builder()
					.artworkId(artworkDto.artworkId)
					.adminCheck(artworkDto.adminCheck)
					.canvasType(artworkDto.canvasType)
					.description(artworkDto.description)
					.height(artworkDto.height)
					.imageUrl(artworkDto.imageUrl)
					.isStandardCanvas(artworkDto.isStandardCanvas)
					.length(artworkDto.length)
					.likeCount(artworkDto.likeCount)
					.price(artworkDto.price)
					.saleStatus(artworkDto.saleStatus)
					.stock(artworkDto.stock)
					.termsAccepted(artworkDto.termsAccepted)
					.title(artworkDto.title)
					.width(artworkDto.width)
					.artist(artworkDto.artistId)
					.canvas(artworkDto.canvasId)
					.category(artworkDto.categoryId)
					.subject(artworkDto.subjectId)
					.type(artworkDto.typeId)
					.build();
		return artwork;			
	}
	public static ArtworkDto fromEntity(Artwork artwork ) {
		ArtworkDto artworkDto = ArtworkDto.builder()
				.artworkId(artwork.getArtworkId())
				.adminCheck(artwork.getAdminCheck())
				.canvasType(artwork.getCanvasType())
				.description(artwork.getDescription())
				.height(artwork.getHeight())
				.imageUrl(artwork.getImageUrl())
				.isStandardCanvas(artwork.getIsStandardCanvas())
				.length(artwork.getLength())
				.likeCount(artwork.getLikeCount())
				.price(artwork.getPrice())
				.saleStatus(artwork.getSaleStatus())
				.stock(artwork.getStock())
				.termsAccepted(artwork.getTermsAccepted())
				.title(artwork.getTitle())
				.width(artwork.getWidth())
				.artistId(artwork.getArtist())
				.canvasId(artwork.getCanvas())
				.categoryId(artwork.getCategory())
				.subjectId(artwork.getSubject())
				.typeId(artwork.getType())
				.build();
		return artworkDto;
	}


	
}
