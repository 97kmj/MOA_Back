package com.moa.mypage.service;

import com.moa.mypage.dto.ArtworkDTO;
import com.moa.entity.Artwork;
import com.moa.repository.ArtworkRepository;
import com.moa.mypage.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepository;

    @Override
    public List<ArtworkDTO> getArtworksByArtist(String username) {
        List<Artwork> artworks = artworkRepository.findByArtistUsername(username);
        return artworks.stream()
            .map(artwork -> ArtworkDTO.builder()
                .artworkId(artwork.getArtworkId())
                .title(artwork.getTitle())
                .description(artwork.getDescription())
                .imageUrl(artwork.getImageUrl())
                .createAt(artwork.getCreateAt())
                .build())
            .collect(Collectors.toList());
    }
}