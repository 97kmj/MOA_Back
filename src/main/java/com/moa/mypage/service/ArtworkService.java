package com.moa.mypage.service;

import com.moa.mypage.dto.ArtworkDTO;
import java.util.List;

public interface ArtworkService {
    List<ArtworkDTO> getArtworksByArtist(String username);
}