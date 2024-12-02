package com.moa.user.service;

import java.util.List;

import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;

public interface ArtistDetailService {
	ArtistDetailDto getArtistInfo(String username) throws Exception;
	List<ArtistArtworkDto> getArtworks(String username, String artworkType) throws Exception;

	Long getTotalArtworksCount(String username) throws Exception;
}
