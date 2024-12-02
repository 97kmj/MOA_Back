package com.moa.shop.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moa.config.image.FolderConstants;
import com.moa.config.image.ImageService;
import com.moa.entity.Artwork;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.entity.Canvas;
import com.moa.entity.LikeArtwork;
import com.moa.entity.User;
import com.moa.repository.ArtworkRepository;
import com.moa.repository.CanvasRepository;
import com.moa.repository.CategoryRepository;
import com.moa.repository.LikeArtworkRepository;
import com.moa.repository.SubjectRepository;
import com.moa.repository.TypeRepository;
import com.moa.repository.UserRepository;
import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.shop.repository.ArtworkDslRepository;
import com.moa.user.dto.OrderUserInfoDto;
import com.moa.user.service.util.PageInfo;

import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

	private final ArtworkRepository artworkRepository;
	private final CategoryRepository categoryRepository;
	private final TypeRepository typeRepository;
	private final SubjectRepository subjectRepository;
	private final CanvasRepository canvasRepository;
	private final ImageService imageService;
	private final UserRepository userRepository;
	private final LikeArtworkRepository likeArtworkRepository;

	@Override
	public Long artworkAdd(ArtworkDto artworkDto, MultipartFile artworkImage) throws Exception {
		String rootFolder = FolderConstants.ARTWORK_ROOT;
		String fileType = FolderConstants.ARTWORK_IMAGE;
		
		String artworkImgeUrl= null;
		
		User artist = userRepository.findById(artworkDto.getArtistId())
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		
		if(artworkImage != null && !artworkImage.isEmpty()) {
			artworkImgeUrl = imageService.saveImage(rootFolder, fileType, artworkImage);
			artworkDto.setImageUrl(artworkImgeUrl);
		}
		
		Artwork artwork = artworkDto.toArtworkEntity();
		artwork.setArtist(artist);

		
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
	public List<CanvasDto> canvasList(String canvasType) throws Exception {
		
		Canvas.CanvasType type = Canvas.CanvasType.valueOf(canvasType);;
		return canvasRepository.findByCanvasType(type) 
	            .stream()
	            .map(CanvasDto::fromEntity)
	            .collect(Collectors.toList());

	}

	@Override
	public List<ArtworkDto> artworkList(Integer page, String category, String keyword, String type, String subject, SaleStatus saleStatus,
			Integer size) throws Exception {
		 	page = (page != null && page > 0) ? page - 1 : 0;
		    size = (size != null && size > 0) ? size : 8;  // 기본 페이지 크기 설정
			PageRequest pageRequest = PageRequest.of(page, size);
			if ((subject != null && !subject.isEmpty()) || (type != null && !type.isEmpty()) || (category != null && !category.isEmpty()) || (keyword != null && !keyword.isEmpty())|| (saleStatus !=null)) {
				List<ArtworkDto> ListArtworkDto = artworkRepository.findBySearches(subject, type, category, keyword, saleStatus, pageRequest)
						.stream().map(a->ArtworkDto.toArtworkDto(a)).collect(Collectors.toList());
				return ListArtworkDto;
			 } else {
		         List<ArtworkDto> ListArtworkDto = artworkRepository.findBySaleStatus(pageRequest)
		            		.stream().map(a->ArtworkDto.toArtworkDto(a)).collect(Collectors.toList());
		            return  ListArtworkDto;
		       }
	}


	@Override
	public Boolean toggleLikeArtwork(String username, Long artworkId) throws Exception {
		Optional<LikeArtwork> olikeArtwork = likeArtworkRepository.findByUser_UsernameAndArtwork_ArtworkId(username, artworkId);
        Artwork artwork = artworkRepository.findById(artworkId)
                .orElseThrow(() -> new IllegalArgumentException("작품을 찾을 수 없습니다."));

        if(olikeArtwork.isPresent()) {
			likeArtworkRepository.deleteById(olikeArtwork.get().getLikeId());
			artwork.setLikeCount(artwork.getLikeCount()-1);
			artworkRepository.save(artwork);

			return false;
		} else {
			likeArtworkRepository.save(new LikeArtwork().builder()
					.artwork(Artwork.builder().artworkId(artworkId).build())
					.user(User.builder().username(username).build())
					.build());
			artwork.setLikeCount(artwork.getLikeCount()+1);
			artworkRepository.save(artwork);
			return true;
		}
	}

	@Override
	public Boolean isLikeArtwork(String username, Long artworkId) throws Exception {
		if(likeArtworkRepository.findByUser_UsernameAndArtwork_ArtworkId(username, artworkId).isPresent()) {
			return true;
		} 
		return false;
	}

	@Override
	public ArtworkDto artworkDetail(Long artworkId) throws Exception {
		return ArtworkDto.toArtworkDto(artworkRepository.findById(artworkId).orElseThrow(()->new Exception("atrwork 번호 오류")));
	}

	@Override
	public OrderUserInfoDto orderUserInfo(String username) throws Exception {
		System.out.println("마지막username:"+username);
		System.out.println("======================");
		
		return OrderUserInfoDto.fromUserInfo(userRepository.findById(username).orElseThrow(()->new Exception("User 번호 오류")));
	}
}
