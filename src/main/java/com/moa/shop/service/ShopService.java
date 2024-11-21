package com.moa.shop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moa.entity.Canvas;
import com.moa.entity.Category;
import com.moa.entity.Subject;
import com.moa.entity.Type;
import com.moa.shop.dto.ArtworkDto;
import com.moa.user.service.util.PageInfo;





public interface ShopService {
	List<ArtworkDto> artworkList(PageInfo page, String category, String type, String subject, String saletype) throws Exception;
	List<ArtworkDto> artworkListByArtist(PageInfo page,String keyword)throws Exception;
	Long artworkAdd(ArtworkDto artworkDto, MultipartFile[] file) throws Exception;
	List<Category> categoryList() throws Exception;
	List<Type> typeList() throws Exception;
	List<Subject> subjectList() throws Exception;
	List<Canvas> canvasType() throws Exception;
}
