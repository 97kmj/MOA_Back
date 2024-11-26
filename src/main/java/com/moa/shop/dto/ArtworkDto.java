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
	
//	@Autowired
//	private static UserRepository userRepository;
//	@Autowired
//	private static CanvasRepository canvasRepository;
//	@Autowired
//	private static CategoryRepository categoryRepository;
//	@Autowired
//	private static SubjectRepository subjectRepository;
//	@Autowired
//	private static TypeRepository typeRepository;
//	
//	
//	public static Artwork toEntity(ArtworkDto artworkDto) throws Exception {
//		Artwork artwork = Artwork.builder()
//				.adminCheck(false)
//				.canvasType(CanvasType.valueOf(artworkDto.getCanvasType()))
//				.description(artworkDto.getDescription())
//				.height(artworkDto.getHeight())
//				.isStandardCanvas(artworkDto.getIsStandardCanvas())
//				.length(artworkDto.getLength())
//				.price(artworkDto.getPrice())
//				.saleStatus(SaleStatus.valueOf(artworkDto.getSaleStatus()))
//				.stock(artworkDto.getStock())
//				.termsAccepted(artworkDto.getTermsAccepted())
//				.title(artworkDto.getTitle())
//				.width(artworkDto.getWidth())
//				.artist(userRepository.findById(artworkDto.getArtistId()).orElseThrow(()->new Exception("artistId오류")))
//				.canvas(canvasRepository.findById(artworkDto.getCanvasId()).orElseThrow(()->new Exception("CanvasId오류")))
//				.category(categoryRepository.findById(artworkDto.getCategoryId()).orElseThrow(()->new Exception("CategoryId오류")))
//				.subject(subjectRepository.findById(artworkDto.getSubjectId()).orElseThrow(()->new Exception("SubjectId오류")))
//				.type(typeRepository.findById(artworkDto.getTypeId()).orElseThrow(()->new Exception("TypeId오류")))
//				.build();
//				
//	return artwork;	
		
//	}
//	public static ArtworkDto fromEntity(Artwork artwork ) {
//		ArtworkDto artworkDto = ArtworkDto.builder()
//				.adminCheck(false)
//				.canvasType( (artwork.getCanvasType()))
//				.description(artwork.getDescription())
//				.height(artwork.getHeight())
//				.imageUrl(artwork.getImageUrl())
//				.isStandardCanvas(artwork.getIsStandardCanvas())
//				.length(artwork.getLength())
//				.likeCount(artwork.getLikeCount())
//				.price(artwork.getPrice())
//				.saleStatus(artwork.getSaleStatus())
//				.stock(artwork.getStock())
//				.termsAccepted(artwork.getTermsAccepted())
//				.title(artwork.getTitle())
//				.width(artwork.getWidth())
//				.artistId(artwork.getArtist().getName())
//				.canvasId(artwork.getCanvas().getCanvasId())
//				.categoryId(artwork.getCategory().getCategoryId())
//				.subjectId(artwork.getSubject().getSubjectId())
//				.typeId(artwork.getType().getTypeId())
//				.build();
//		return artworkDto;
//	}


	
}
