package com.moa.shop.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.moa.entity.Artwork;
import com.moa.entity.Artwork.CanvasType;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.repository.CanvasRepository;
import com.moa.repository.CategoryRepository;
import com.moa.repository.SubjectRepository;
import com.moa.repository.TypeRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.ArtworkDto;

@Component
public class ArtworkAddMapper {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CanvasRepository canvasRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private TypeRepository typeRepository;

	public Artwork toArtworkEntity(ArtworkDto artworkDto, String ArtworkImgeUrl) throws Exception {

		return Artwork.builder()
			.adminCheck(false)
			.canvasType(CanvasType.valueOf(artworkDto.getCanvasType()))
			.description(artworkDto.getDescription()!= null ? artworkDto.getDescription() : "") 
			.height(artworkDto.getHeight()!= null ? artworkDto.getHeight(): "")
			.imageUrl(ArtworkImgeUrl)
			.isStandardCanvas(artworkDto.getIsStandardCanvas())
			.length(artworkDto.getLength()!= null ? artworkDto.getLength() :"")
			.price(artworkDto.getPrice()!= null ? artworkDto.getPrice() : 0)
			.saleStatus(SaleStatus.valueOf(artworkDto.getSaleStatus()))
			.stock(artworkDto.getStock()!= null ? artworkDto.getStock(): 0)
			.termsAccepted(true)
			.title(artworkDto.getTitle())
			.width(artworkDto.getWidth()!=null ? artworkDto.getWidth() : "" )
			.artist(userRepository.findByUsername(artworkDto.getArtistId()).orElseThrow(()->new Exception("artistId오류")))
			.canvas(canvasRepository.findById(artworkDto.getCanvasId()).orElseThrow(()->new Exception("CanvasId오류")))
			.category(categoryRepository.findById(artworkDto.getCategoryId()).orElseThrow(()->new Exception("CategoryId오류")))
			.subject(subjectRepository.findById(artworkDto.getSubjectId()).orElseThrow(()->new Exception("SubjectId오류")))
			.type(typeRepository.findById(artworkDto.getTypeId()).orElseThrow(()->new Exception("TypeId오류")))
			.build();
		
	}
}
