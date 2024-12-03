package com.moa.user.service;

import java.util.List;

import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;
import com.moa.user.dto.SendMessageDto;

public interface ArtistDetailService {
	//작가 정보 가져오기
	ArtistDetailDto getArtistInfo(String artistId) throws Exception;
	//갤러리,판매중,판매완료 타입에 따른 작품목록 가져오기
	List<ArtistArtworkDto> getArtworks(String artistId, String artworkType, String username) throws Exception;
	//작가 좋아요했는지 여부
	Boolean getLikeArtist(String artistId, String username) throws Exception;
	//작가 좋아요 토글
	Boolean toggleLikeArtist(String artistId, String username) throws Exception;
	//총 작품 갯수 
	Long getTotalArtworksCount(String username) throws Exception;
	
	//쪽지 보내기
	void sendMessage(SendMessageDto message) throws Exception;
}
