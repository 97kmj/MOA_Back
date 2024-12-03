package com.moa.user.dto;

import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDetailDto {
	
	private String name;
	private Integer likeCount;
	private String artistNote;
	private String artistCareer;
	private String profileImage;
	private Long totalArtworkCount;
	
	public static ArtistDetailDto fromEntity(User user) {
		return ArtistDetailDto.builder()
				.name(user.getName())
				.likeCount(user.getLikeCount())
				.artistNote(user.getArtistNote())
				.artistCareer(user.getArtistCareer())
				.profileImage(user.getProfileImage())
				.build();
	}
}
