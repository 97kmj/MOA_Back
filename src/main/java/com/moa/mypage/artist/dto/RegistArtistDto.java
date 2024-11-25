package com.moa.mypage.artist.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistArtistDto {
	private String username;
	private MultipartFile portfolio;
	private MultipartFile profileImage;
	private String artistNote;
	private String artistCareer;
}
