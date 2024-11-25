package com.moa.mypage.artist.service;

import com.moa.mypage.artist.dto.RegistArtistDto;

public interface MyPageArtistService {
	void registArtist(RegistArtistDto registArtistDto) throws Exception;
	void modifyArtistInfo() throws Exception;
}
