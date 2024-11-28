package com.moa.gallery.service;

import com.moa.config.jwt.JwtToken;
import com.moa.entity.Artwork;
import com.moa.repository.ArtworkRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtworkService {

    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private JwtToken jwtToken; // JWT 토큰 처리용 서비스

    public List<Artwork> getArtworks(String subject, String type, String category, String search,
        int page, int size) {
        // 페이징 처리
        PageRequest pageRequest = PageRequest.of(page, size);

        // 필터링 조건 적용
        if (subject != null || type != null || category != null || search != null) {
            return artworkRepository.findByFilters(subject, type, category, search, pageRequest)
                .getContent();
        } else {
            return artworkRepository.findAll(pageRequest).getContent();
        }
    }

    public Optional<Artwork> getArtworkById(Long id) {

        return artworkRepository.findById(id);
    }



}