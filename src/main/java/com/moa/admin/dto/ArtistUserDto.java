package com.moa.admin.dto;

import java.sql.Timestamp;

import com.moa.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistUserDto {
	private String username;
	private String name;
	private String portfolioUrl;
	private String profileImage;
	private String artistNote; //작가 노트
	private String artistCareer; // 작가 이력
	private Timestamp applicationDate; // 신청일자
	
	public static ArtistUserDto fromEntity(User user) {
		return ArtistUserDto.builder()
					.username(user.getUsername())
					.name(user.getName())
					.portfolioUrl(user.getPortfolioUrl())
					.profileImage(user.getProfileImage())
					.artistNote(user.getArtistNote())
					.artistCareer(user.getArtistCareer())
					.applicationDate(user.getApplicationDate())
					.build();
	}
}
