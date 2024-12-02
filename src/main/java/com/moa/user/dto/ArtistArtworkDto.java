package com.moa.user.dto;

import com.moa.entity.Artwork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistArtworkDto {
	private Long artworkId;
	private String title;
	private String imageUrl;
	
	public static ArtistArtworkDto fromEntity(Artwork artwork) {
		return ArtistArtworkDto.builder()
				.artworkId(artwork.getArtworkId())
				.title(artwork.getTitle())
				.imageUrl(artwork.getImageUrl())
				.build();
	}
}
