package com.moa.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.moa.entity.LikeArtist;
import com.moa.entity.Message;
import com.moa.entity.User;
import com.moa.repository.LikeArtistRepository;
import com.moa.repository.LikeArtworkRepository;
import com.moa.repository.MessageRepository;
import com.moa.repository.UserRepository;
import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;
import com.moa.user.dto.SendMessageDto;
import com.moa.user.repository.ArtistDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistDetailServiceImpl implements ArtistDetailService {

	private final UserRepository userRepository;
	private final ArtistDetailRepository artistDetailRepository;
	private final LikeArtistRepository likeArtistRepository;
	private final LikeArtworkRepository likeArtworkRepository;
	private final MessageRepository messageRepository;
	@Override
	public ArtistDetailDto getArtistInfo(String artistId) throws Exception {
		User artist = userRepository.findById(artistId).orElseThrow(()->new Exception("username오류"));
		return ArtistDetailDto.fromEntity(artist);
	}
	@Override
	public List<ArtistArtworkDto> getArtworks(String artistId, String artworkType, String username) throws Exception {
		//좋아요 누른 작품ID리스트 가져오기 
		List<Long> likedArtworkIds = likeArtworkRepository.findArtworkIdsByUsername(username);
		List<ArtistArtworkDto> artworkList = artistDetailRepository.selectArtworksByUsernameAndType(artistId, artworkType)
				.stream()
				.map(a->{
					ArtistArtworkDto dto = ArtistArtworkDto.fromEntity(a);
					dto.setIsLiked(likedArtworkIds.contains(dto.getArtworkId()));
					return dto;
					})
				.collect(Collectors.toList());

		return artworkList;
	}
	@Override
	public Boolean getLikeArtist(String artistId, String username) throws Exception {
		return artistDetailRepository.existsLikeArtist(artistId, username);
	}
	
	@Override
	public Boolean toggleLikeArtist(String artistId, String username) throws Exception {
		Optional<LikeArtist> like = likeArtistRepository.findByArtist_UsernameAndUser_Username(artistId,username);
		User artist = userRepository.findById(artistId).orElseThrow(()->new Exception("artistId 오류"));
		User user = userRepository.findById(username).orElseThrow(()->new Exception("username 오류"));
		if(like.isPresent()) {
			likeArtistRepository.delete(like.get());
			artist.setLikeCount(artist.getLikeCount()-1);
			userRepository.save(artist);
			return false;
		} else {
			LikeArtist newLike = LikeArtist.builder()
					.artist(artist)
					.user(user)
					.build();
			likeArtistRepository.save(newLike);
			artist.setLikeCount(artist.getLikeCount()+1);
			userRepository.save(user);
			return true;
		}
	}
	
	@Override
	public Long getTotalArtworksCount(String username) throws Exception {
		return artistDetailRepository.selectArtworkCount(username);
	}
	
	@Override
	public void sendMessage(SendMessageDto message) throws Exception {
		Message sendMessage = Message.builder()
								.title(message.getTitle())
								.content(message.getContent())
								.receiver(userRepository.findById(message.getArtistId()).orElseThrow(()->new Exception("받는 사람 아이디 오류")))
								.sender(userRepository.findById(message.getUsername()).orElseThrow(()->new Exception("보내는 사람 아이디 오류")))
								.build();
		messageRepository.save(sendMessage);
	}
	

}
