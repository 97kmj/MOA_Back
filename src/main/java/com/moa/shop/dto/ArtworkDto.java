package com.moa.shop.dto;


import com.moa.entity.Artwork;
import com.moa.entity.Canvas;
import com.moa.entity.Category;
import com.moa.entity.Subject;
import com.moa.entity.Type;
import com.moa.entity.User;
import com.moa.entity.Artwork.SaleStatus;



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
	private String artisName;
	private Long canvasId;
	private String canvasNum;
	private Integer categoryId;
	private String categoryName;
	private Integer subjectId;
	private String subjectName;
	private Integer typeId;
	private String typeName;

	public Artwork toArtworkEntity() throws Exception {
		return Artwork.builder()
			.adminCheck(false)
		
			.description(description != null ? description : "") 
			.height(height!= null ? height: "")
			.imageUrl(imageUrl)
			.isStandardCanvas(isStandardCanvas)
			.length(length!= null ? length :"")
			.price(price!= null ? price : 0)
			.saleStatus(SaleStatus.valueOf(saleStatus))
			.stock(stock!= null ? stock: 0)
			.termsAccepted(true)
			.title(title)
			.width(width!=null ? width : "" )
			.artist(User.builder().username(artistId).build())
			.canvas(canvasId != null ? Canvas.builder().canvasId(canvasId).build() : null)
			.category(Category.builder().categoryId(categoryId).build())
			.subject(Subject.builder().subjectId(subjectId).build())
			.type(Type.builder().typeId(typeId).build())
			.build();
	}
	
//	public static ArtworkDto toArtworkDto(Artwork artwork) throws Exception {
//		return ArtworkDto.builder()
//				.artworkId(artwork.getArtworkId())
//				.adminCheck(artwork.getAdminCheck())
//				.canvasType(artwork.getCanvasType().toString())
//				.description(artwork.getDescription())
//				.height(artwork.getHeight())
//				.imageUrl(artwork.getImageUrl())
//				.isStandardCanvas(artwork.getIsStandardCanvas())
//				.length(artwork.getLength())
//				.likeCount(artwork.getLikeCount())
//				.price(artwork.getPrice())
//				.saleStatus(artwork.getSaleStatus().toString())
//				.stock(artwork.getStock())
//				.termsAccepted(artwork.getTermsAccepted())
//				.title(artwork.getTitle())
//				.width(artwork.getWidth())
//				.artistId(artwork.getArtist().getUsername())
//				.artisName(artwork.getArtist().getName())
//				.canvasId(artwork.getCanvas().getCanvasId())
//				.canvasNum(artwork.getCanvas().getCanvasNum().toString())
//				.categoryId(artwork.getCategory().getCategoryId())
//				.categoryName(artwork.getCategory().getCategoryName())
//				.subjectId(artwork.getSubject().getSubjectId())
//				.subjectName(artwork.getSubject().getSubjectName())
//				.typeId(artwork.getType().getTypeId())
//				.typeName(artwork.getType().getTypeName())
//				.build();
//		
//	}
	public static ArtworkDto toArtworkDto(Artwork artwork) {
	    // Null 체크를 통해 예외를 방지하고, null일 경우 기본값을 사용하거나 null을 반환
	    return ArtworkDto.builder()
	            .artworkId(artwork.getArtworkId())
	            .adminCheck(artwork.getAdminCheck() != null ? artwork.getAdminCheck() : false) // null일 경우 false로 설정
	            .canvasType(artwork.getCanvas() != null && artwork.getCanvas().getCanvasType() != null? artwork.getCanvas().getCanvasType().toString(): "") // null이면 빈 문자열
	            .description(artwork.getDescription() != null ? artwork.getDescription() : "") // null이면 빈 문자열
	            .height(artwork.getHeight() != null ? artwork.getHeight() : "") // null이면 빈 문자열
	            .imageUrl(artwork.getImageUrl() != null ? artwork.getImageUrl() : "") // null이면 빈 문자열
	            .isStandardCanvas(artwork.getIsStandardCanvas() != null ? artwork.getIsStandardCanvas() : false) // null이면 false
	            .length(artwork.getLength() != null ? artwork.getLength() : "") // null이면 빈 문자열
	            .likeCount(artwork.getLikeCount() != null ? artwork.getLikeCount() : 0) // null이면 0
	            .price(artwork.getPrice() != null ? artwork.getPrice() : 0L) // null이면 0L
	            .saleStatus(artwork.getSaleStatus() != null ? artwork.getSaleStatus().toString() : "") // null이면 빈 문자열
	            .stock(artwork.getStock() != null ? artwork.getStock() : 0) // null이면 0
	            .termsAccepted(artwork.getTermsAccepted() != null ? artwork.getTermsAccepted() : false) // null이면 false
	            .title(artwork.getTitle() != null ? artwork.getTitle() : "") // null이면 빈 문자열
	            .width(artwork.getWidth() != null ? artwork.getWidth() : "") // null이면 빈 문자열
	            .artistId(artwork.getArtist() != null && artwork.getArtist().getUsername() != null ? artwork.getArtist().getUsername() : "") // null이면 빈 문자열
	            .artisName(artwork.getArtist() != null && artwork.getArtist().getName() != null ? artwork.getArtist().getName() : "") // null이면 빈 문자열
	            .canvasId(artwork.getCanvas() != null && artwork.getCanvas().getCanvasId() != null ? artwork.getCanvas().getCanvasId() : 0L) // null이면 0L
	            .canvasNum(artwork.getCanvas() != null && artwork.getCanvas().getCanvasNum() != null ? artwork.getCanvas().getCanvasNum().toString() : "") // null이면 빈 문자열
	            .categoryId(artwork.getCategory() != null && artwork.getCategory().getCategoryId() != null ? artwork.getCategory().getCategoryId() : 1) // null이면 0L
	            .categoryName(artwork.getCategory() != null && artwork.getCategory().getCategoryName() != null ? artwork.getCategory().getCategoryName() : "없음") // null이면 빈 문자열
	            .subjectId(artwork.getSubject() != null && artwork.getSubject().getSubjectId() != null ? artwork.getSubject().getSubjectId() : 1) // null이면 0L
	            .subjectName(artwork.getSubject() != null && artwork.getSubject().getSubjectName() != null ? artwork.getSubject().getSubjectName() : "") // null이면 빈 문자열
	            .typeId(artwork.getType() != null && artwork.getType().getTypeId() != null ? artwork.getType().getTypeId() : 1) // null이면 0L
	            .typeName(artwork.getType() != null && artwork.getType().getTypeName() != null ? artwork.getType().getTypeName() : "") // null이면 빈 문자열
	            .build();
	}
}
