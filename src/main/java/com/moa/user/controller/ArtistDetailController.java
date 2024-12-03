package com.moa.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.user.dto.ArtistArtworkDto;
import com.moa.user.dto.ArtistDetailDto;
import com.moa.user.service.ArtistDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArtistDetailController {
	private final ArtistDetailService artistDetailService;
	
	@PostMapping("/artistDetail/{username}")
	public ResponseEntity<ArtistDetailDto> artistDetail(@PathVariable String username) {
		try {
			Map<String,Object> response = new HashMap<>();
			ArtistDetailDto artist = artistDetailService.getArtistInfo(username);
			artist.setTotalArtworkCount(artistDetailService.getTotalArtworksCount(username));
			
			return new ResponseEntity<ArtistDetailDto>(artist,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ArtistDetailDto>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/artistArtworks")
	public ResponseEntity<List<ArtistArtworkDto>> getArtworksByType(@RequestParam("artistId") String artistId, @RequestParam("artworkType") String artworkType) {
		try {
			List<ArtistArtworkDto> artworks = artistDetailService.getArtworks(artistId, artworkType);
			return new ResponseEntity<List<ArtistArtworkDto>>(artworks,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ArtistArtworkDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
