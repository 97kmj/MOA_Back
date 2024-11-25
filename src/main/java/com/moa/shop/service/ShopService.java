package com.moa.shop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.user.service.util.PageInfo;





public interface ShopService {
	List<ArtworkDto> artworkList(PageInfo page, String category, String type, String subject, String saletype) throws Exception;
	List<ArtworkDto> artworkListByArtist(PageInfo page,String keyword)throws Exception;
	Long artworkAdd(ArtworkDto artworkDto) throws Exception;
	List<CategoryDto> categoryList() throws Exception;
	List<CanvasDto> canvasList() throws Exception;
	List<TypeDto> typeList(Integer categoryId) throws Exception;
	List<SubjectDto> subjectList(Integer categoryId) throws Exception;
}
