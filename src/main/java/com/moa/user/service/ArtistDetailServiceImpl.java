package com.moa.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moa.entity.User;
import com.moa.repository.UserRepository;
import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;
import com.moa.user.repository.ArtistDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistDetailServiceImpl implements ArtistDetailService {

	private final UserRepository userRepository;
	private final ArtistDetailRepository artistDetailRepository;
	@Override
	public ArtistDetailDto getArtistInfo(String username) throws Exception {
		User artist = userRepository.findById(username).orElseThrow(()->new Exception("username오류"));
		return ArtistDetailDto.fromEntity(artist);
	}
	@Override
	public List<ArtistArtworkDto> getArtworks(String username, String artworkType) throws Exception {
		return artistDetailRepository.selectArtworksByUsernameAndType(username, artworkType).stream().map(a->ArtistArtworkDto.fromEntity(a)).collect(Collectors.toList());
	}
	
	@Override
	public Long getTotalArtworksCount(String username) throws Exception {
		return artistDetailRepository.selectArtworkCount(username);
	}
	

}
