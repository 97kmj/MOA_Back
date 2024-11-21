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
	private String canvasType;
	private String description;
	private String height;
	private String image_url;
	private Integer isStandardCanvas;
	private String length;
	private Integer likeCount;
	private Long price;
	private String saleStatus;
	private Integer stock;
	private Integer termsAccepted;
	private String title;
	private String width;
	private User artistId;
	private Canvas canvasId;
	private Category categoryId;
	private Subject subjectId;
	private Type typeId;
	
	public Artwork toEntity() {
		Artwork artwork = Artwork.builder()
					.artworkId(artworkId)
					.adminCheck(adminCheck)
					.canvasType(CanvasType.valueOf(canvasType))
					.description(description)
					.height(height)
					.imageUrl(image_url)
					.isStandardCanvas(adminCheck)
					.length(length)
					.likeCount(likeCount)
					.price(price)
					.saleStatus(SaleStatus.valueOf(saleStatus))
					.stock(stock)
					.termsAccepted(adminCheck)
					.title(title)
					.width(width)
					.artist(artistId)
					.canvas(canvasId)
					.category(categoryId)
					.subject(subjectId)
					.type(typeId)
					.build();
					
					
		return artwork;			
	}
}
