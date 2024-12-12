package com.moa.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.moa.admin.dto.FrameDto;
import com.moa.entity.Artwork.SaleStatus;
import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.user.dto.OrderUserInfoDto;





public interface ShopService {
	Long artworkAdd(ArtworkDto artworkDto,MultipartFile artworkImage) throws Exception;
	List<CategoryDto> categoryList() throws Exception;
	List<CanvasDto> canvasList(String canvasType) throws Exception;
	List<TypeDto> typeList(Integer categoryId) throws Exception;
	List<SubjectDto> subjectList(Integer categoryId) throws Exception;
	Map<String,Object> artworkList(Integer page, Integer category, String keyword, Integer type, Integer subject,SaleStatus saleStatus, Integer size) throws Exception;
	Boolean toggleLikeArtwork(String username, Long artworkId) throws Exception;  //
	Boolean isLikeArtwork(String username, Long artworkId) throws Exception;  //
	ArtworkDto artworkDetail(Long artworkId) throws Exception;
	OrderUserInfoDto orderUserInfo (String username) throws Exception;
	List<FrameDto> frameListByCanvasId (Long canvasId) throws Exception;
	List<FrameDto> frameList (Long frameOptionId) throws Exception;
	
}
