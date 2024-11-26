package com.moa.mypage.artist.service;

import org.springframework.web.multipart.MultipartFile;

import com.moa.mypage.artist.dto.RegistArtistDto;

public interface MyPageArtistService {
	void registArtist(RegistArtistDto registArtistDto, MultipartFile portfolio, MultipartFile profileImage) throws Exception;
	void modifyArtistInfo() throws Exception;
}
