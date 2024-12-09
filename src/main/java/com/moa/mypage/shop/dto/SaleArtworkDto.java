package com.moa.mypage.shop.dto;

import java.util.Date;

import com.moa.entity.Artwork;
import com.moa.entity.Order;
import com.moa.shop.dto.ArtworkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleArtworkDto {
	private Long artworkId;
	private String saleStatus;
	private Long price;
	private String title;
	private String artistId;
	private String artistName;
	private String imageUrl;
	private Date createAt;
	
	public static SaleArtworkDto toArtworkDto(Artwork artwork){
		return SaleArtworkDto.builder()
				.artworkId(artwork.getArtworkId())
				.saleStatus(artwork.getSaleStatus() != null ? artwork.getSaleStatus().toString() : "") // null이면 빈 문자열
				.title(artwork.getTitle() != null ? artwork.getTitle() : "") // null이면 빈 문자열
				.artistId(artwork.getArtist() != null && artwork.getArtist().getUsername() != null ? artwork.getArtist().getUsername() : "") // null이면 빈 문자열
				.price(artwork.getPrice() != null ? artwork.getPrice() : 0L) // null이면 0L
				.artistName(artwork.getArtist() != null && artwork.getArtist().getName() != null ? artwork.getArtist().getName() : "") // null이면 빈 문자열
				.imageUrl(artwork.getImageUrl() != null ? artwork.getImageUrl() : "") // null이면 빈 문자열
				.build();
	}
}
