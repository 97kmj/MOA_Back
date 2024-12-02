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
public class MainArtworkDto {
	private Long artworkId;
	private String imageUrl;
	
	public static MainArtworkDto fromEntity(Artwork artwork) {
		return MainArtworkDto.builder()
				.artworkId(artwork.getArtworkId())
				.imageUrl(artwork.getImageUrl())
				.build();
	}
}
