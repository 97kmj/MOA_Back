package com.moa.mypage.shop.dto;

import java.util.Date;

import com.moa.entity.Artwork;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderArtworkDto {
	private Long artworkId;
	private Long price;
	private String title;
	private String artistName;
	private String imageUrl;
	private Date paymentDate;
	private Long orderId;
	private Integer quantity;


    
}
