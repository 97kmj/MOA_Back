package com.moa.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Artwork;
import com.moa.entity.Artwork.CanvasType;
import com.moa.entity.Canvas;
import com.moa.entity.Category;
import com.moa.entity.Subject;
import com.moa.entity.Type;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.CanvasRepository;
import com.moa.repository.CategoryRepository;
import com.moa.repository.SubjectRepository;
import com.moa.repository.TypeRepository;
import com.moa.shop.dto.ArtworkDto;
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
	public Long artworkAdd(ArtworkDto artworkDto, MultipartFile[] file) throws Exception {
		Artwork artwork = artworkDto.toEntity();
		artworkRepository.save(artwork);
		
		return artwork.getArtworkId();
	}

	@Override
	public List<Category> categoryList() throws Exception {
		List<Category> categoryList = categoryRepository.findAll();
		return categoryList;
	}

	@Override
	public List<Type> typeList() throws Exception {
		List<Type> typeList = typeRepository.findAll();
		return typeList;
	}

	@Override
	public List<Subject> subjectList() throws Exception {
		List<Subject> subjectList = subjectRepository.findAll();
		return subjectList;
	}

	@Override
	public List<Canvas> canvasType() throws Exception {
		List<Canvas> canvasType = canvasRepository.findAll();
		return canvasType;
	}

}
