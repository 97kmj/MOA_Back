package com.moa.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Artwork;
import com.moa.repository.ArtworkRepository;
import com.moa.shop.dto.ArtworkDto;
import com.moa.user.service.util.PageInfo;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ArtworkRepository artworkRepository;
	
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

}
