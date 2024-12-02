package com.moa.mypage.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtworkDTO {
    private Long artworkId;
    private String title;
    private String description;
    private String imageUrl;
    private Timestamp createAt;
}