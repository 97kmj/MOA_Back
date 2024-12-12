package com.moa.gallery.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.entity.Artwork;
import com.moa.gallery.service.ArtworkService;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;


    // 전체 작품 리스트 가져오기
    @GetMapping
    public Page<Artwork> getAllArtworks(
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "NOT_SALE") Artwork.SaleStatus saleStatus, // Enum 타입으로 변경
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "8") int size) {
        return artworkService.getArtworks(saleStatus, subject, type, category, search, page, size);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getArtworkDetails(@PathVariable Long id) {
        try {
            // ArtworkService에서 제공하는 getArtworkById 메서드 활용
            Optional<Artwork> artworkOptional = artworkService.getArtworkById(id);

            if (artworkOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("작품을 찾을 수 없습니다.");
            }

            return ResponseEntity.ok(artworkOptional.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("작품 정보를 가져오는 데 실패했습니다.");
        }
    }


}
