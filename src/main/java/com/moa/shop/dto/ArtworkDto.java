package com.moa.shop.dto;


//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.moa.entity.Artwork;
//import com.moa.entity.Artwork.CanvasType;
//import com.moa.entity.Artwork.SaleStatus;
//import com.moa.entity.Canvas;
//import com.moa.entity.Category;
//import com.moa.entity.Subject;
//import com.moa.entity.Type;
//import com.moa.repository.CanvasRepository;
//import com.moa.repository.CategoryRepository;
//import com.moa.repository.SubjectRepository;
//import com.moa.repository.TypeRepository;
//import com.moa.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtworkDto {
	private Long artworkId;
	private Boolean adminCheck;
	private String canvasType;
	private String description;
	private String height;
	private String imageUrl;
	private Boolean isStandardCanvas;
	private String length;
	private Integer likeCount;
	private Long price;
	private String saleStatus;
	private Integer stock;
	private Boolean termsAccepted;
	private String title;
	private String width;
	private String artistId;
	private Long canvasId;
	private Integer categoryId;
	private Integer subjectId;
	private Integer typeId;	

}
