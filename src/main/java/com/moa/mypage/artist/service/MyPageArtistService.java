package com.moa.mypage.artist.service;

import org.springframework.web.multipart.MultipartFile;

import com.moa.mypage.artist.dto.EditArtistDto;
import com.moa.mypage.artist.dto.RegistArtistDto;

public interface MyPageArtistService {
	void registArtist(RegistArtistDto registArtistDto, MultipartFile portfolio, MultipartFile profileImage) throws Exception;
	EditArtistDto getArtistInfo(String username) throws Exception;
	void modifyArtistInfo(EditArtistDto editArtistDto, MultipartFile profileImage) throws Exception;
}
