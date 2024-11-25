package com.moa.shop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.Artwork;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.CanvasRepository;
import com.moa.repository.CategoryRepository;
import com.moa.repository.SubjectRepository;
import com.moa.repository.TypeRepository;
import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.shop.mapper.ArtworkAddMapper;
import com.moa.user.service.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

	private final ArtworkRepository artworkRepository;
	private final CategoryRepository categoryRepository;
	private final TypeRepository typeRepository;
	private final SubjectRepository subjectRepository;
	private final CanvasRepository canvasRepository;
	private final ImageService imageService;
	
	
	
	@Override
	public List<ArtworkDto> artworkList(PageInfo page, String category, String type, String subject, String saletype)throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArtworkDto> artworkListByArtist(PageInfo page, String keyword) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long artworkAdd(ArtworkDto artworkDto, MultipartFile artworkImage) throws Exception {
		String rootFolder = FolderConstants.ARTWORK_ROOT;
		String fileType = FolderConstants.ARTWORK_IMAGE;
		
		String ArtworkImgeUrl= null;
		
		if(artworkImage != null && !artworkImage.isEmpty()) {
			ArtworkImgeUrl = imageService.saveImage(rootFolder, fileType, artworkImage);
		}
		
		Artwork artwork = ArtworkAddMapper.toArtworkEntity(artworkDto, ArtworkImgeUrl);
		
		artworkRepository.save(artwork);
		return artwork.getArtworkId();
	
	}

	@Override
	public List<CategoryDto> categoryList() throws Exception {
		List<CategoryDto> categoryList = categoryRepository.findAll().stream().map(c->
		CategoryDto.fromEntity(c)).collect(Collectors.toList());
		return categoryList;
	}

	@Override
	public List<TypeDto> typeList(Integer categoryId) throws Exception {
		List<TypeDto> typeList = typeRepository.findByCategory_CategoryId(categoryId).stream().map(c->
		TypeDto.fromEntity(c)).collect(Collectors.toList());
		return typeList;
	}

	@Override
	public List<SubjectDto> subjectList(Integer categoryId) throws Exception {
		List<SubjectDto> subjectList = subjectRepository.findByCategory_CategoryId(categoryId).stream().map(c->
		SubjectDto.fromEntity(c)).collect(Collectors.toList());
		return subjectList;
	}

	@Override
	public List<CanvasDto> canvasList() throws Exception {
		List<CanvasDto> canvasList = canvasRepository.findAll().stream().map(c->
		CanvasDto.fromEntity(c)).collect(Collectors.toList());
		return canvasList;
	}

}
