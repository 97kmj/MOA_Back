package com.moa.admin.dto;

import java.sql.Timestamp;

import com.moa.entity.Artwork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackArtworkDto {
	private Long artworkId;
	private String artistId;
	private String title;
	private Timestamp createAt;
	private String description;
	private String imageUrl;
	
	public static BlackArtworkDto fromEntity(Artwork artwork) {
		return BlackArtworkDto.builder()
					.artworkId(artwork.getArtworkId())
					.artistId(artwork.getArtist().getUsername())
					.title(artwork.getTitle())
					.createAt(artwork.getCreateAt())
					.description(artwork.getDescription())
					.imageUrl(artwork.getImageUrl())
					.build();
	}
}
