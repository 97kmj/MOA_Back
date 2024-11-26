package com.moa.mypage.artist.dto;

import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditArtistDto {
	private String username;
	private String profileImage;
	private String artistCareer;
	private String artistNote;
	
	public static EditArtistDto fromEntity(User user) {
		return EditArtistDto.builder()
					.username(user.getUsername())
					.profileImage(user.getProfileImage())
					.artistCareer(user.getArtistCareer())
					.artistNote(user.getArtistNote())
					.build();
	}
	
}
