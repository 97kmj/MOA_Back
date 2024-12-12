package com.moa.gallery.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.moa.config.jwt.JwtToken;
import com.moa.entity.Artwork;
import com.moa.repository.ArtworkRepository;

@Service
public class ArtworkService {

    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private JwtToken jwtToken; // JWT 토큰 처리용 서비스

    public Page<Artwork> getArtworks(Artwork.SaleStatus saleStatus, String subject, String type, String category, String search,
        int page, int size) {
        // 페이징 처리
        PageRequest pageRequest = PageRequest.of(page, size);

        // 필터링 조건 적용
        return artworkRepository.findByFilters(subject, type, category, search, saleStatus, pageRequest);
    }

    public Optional<Artwork> getArtworkById(Long id) {

        return artworkRepository.findById(id);
    }



}