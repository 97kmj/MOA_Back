package com.moa.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;
import com.moa.user.dto.SendMessageDto;
import com.moa.user.service.ArtistDetailService;
import com.moa.user.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArtistDetailController {
	private final ArtistDetailService artistDetailService;
	private final LikeService likeService;
	
	@PostMapping("/artistDetail/{username}")
	public ResponseEntity<ArtistDetailDto> artistDetail(@PathVariable String username) {
		try {
			
			ArtistDetailDto artist = artistDetailService.getArtistInfo(username);
			artist.setTotalArtworkCount(artistDetailService.getTotalArtworksCount(username));
			return new ResponseEntity<ArtistDetailDto>(artist,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ArtistDetailDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/existsLikeArtist")
	public ResponseEntity<Boolean> existsLikeArtist(@RequestParam("artistId") String artistId, @RequestParam("username") String username) {
		try {
			Boolean existsLike = artistDetailService.getLikeArtist(artistId, username);
			return new ResponseEntity<Boolean>(existsLike,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/artistArtworks")
	public ResponseEntity<List<ArtistArtworkDto>> getArtworksByType(@RequestParam("artistId") String artistId, 
			@RequestParam("artworkType") String artworkType,
			@RequestParam(required=false) String username) {
		try {
			List<ArtistArtworkDto> artworks = artistDetailService.getArtworks(artistId, artworkType,username);
			return new ResponseEntity<List<ArtistArtworkDto>>(artworks,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ArtistArtworkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/likeArtist")
	public ResponseEntity<Boolean> toggleLikeArtist(@RequestParam("artistId") String artistId, @RequestParam("username") String username) {
		try {
			Boolean isLike = artistDetailService.toggleLikeArtist(artistId, username);
			return new ResponseEntity<Boolean>(isLike,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/likeArtwork")
	public ResponseEntity<Boolean> toggleLikeArtwork(@RequestParam("artworkId") Long artworkId, @RequestParam("username") String username) {
		try {
			Boolean isLike = likeService.toggleLike(username, artworkId);
			return new ResponseEntity<Boolean>(isLike,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/sendMessage")
	public ResponseEntity<String> sendMessage(@RequestBody SendMessageDto message) {
		try {
			artistDetailService.sendMessage(message);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
}
