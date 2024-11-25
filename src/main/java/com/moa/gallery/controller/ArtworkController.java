package com.moa.gallery.controller;

import com.moa.entity.Artwork;
import com.moa.gallery.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    @Autowired
    private ArtworkService artworkService;

    // 전체 작품 리스트 가져오기
    @GetMapping
    public List<Artwork> getAllArtworks(
        @RequestParam(required = false) String subject,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "8") int size) {
        return artworkService.getArtworks(subject, type, category, page, size);
    }
}
