package com.moa.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.entity.Artwork;
import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.shop.service.ShopService;
import com.moa.user.service.util.PageInfo;

@RestController
public class ShopController {
	
	@Autowired
	private ShopService shopService;
	
	
	@GetMapping("shop/artworkAdd")
	public ResponseEntity<List<CategoryDto>> categoryList(){
		try {
			List<CategoryDto> categoryList = shopService.categoryList();
			return new ResponseEntity<List<CategoryDto>>(categoryList, HttpStatus.OK); 
			
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<List<CategoryDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/shop/artworkAdd/canvas/{canvasType}")
	public ResponseEntity<List<CanvasDto>> CanvasList(@PathVariable("canvasType") String canvasType){
		try {
			
			canvasType = canvasType.toUpperCase();
			List<CanvasDto> canvasList = shopService.canvasList(canvasType);
			return new ResponseEntity<List<CanvasDto>>(canvasList, HttpStatus.OK); 
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CanvasDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@PostMapping("shop/artworkAdd/type/{categoryId}")
	public ResponseEntity<List<TypeDto>> typeListByCategory(@PathVariable Integer categoryId) {
        try {
        	List<TypeDto> typeList = shopService.typeList(categoryId);
        	return new ResponseEntity<List<TypeDto>>(typeList, HttpStatus.OK);
        }catch(Exception e){
        	e.printStackTrace();
			return new ResponseEntity<List<TypeDto>>(HttpStatus.BAD_REQUEST);
        }
        
    }
	@PostMapping("shop/artworkAdd/subject/{categoryId}")
	public ResponseEntity<List<SubjectDto>> subjectListByCategory(@PathVariable Integer categoryId) {
        try {
        	List<SubjectDto> subjectList = shopService.subjectList(categoryId);
        	return new ResponseEntity<List<SubjectDto>>(subjectList, HttpStatus.OK);
        }catch(Exception e){
        	e.printStackTrace();
			return new ResponseEntity<List<SubjectDto>>(HttpStatus.BAD_REQUEST);
        } 
    }
	
	// 작품등록
	@PostMapping("/shop/artworkAdd")
	public ResponseEntity<Long> artworkAdd(@RequestPart("artworkDto") ArtworkDto artworkDto,
			@RequestPart("artworkImage")MultipartFile artworkImage){
			try {
				
				System.out.println("artwork :" + artworkDto);
				System.out.println("artworkImage :" + artworkImage);
				
				Long artworkNum = shopService.artworkAdd(artworkDto,artworkImage);
				return new ResponseEntity<Long>(artworkNum,HttpStatus.OK);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
			}
		}

	@GetMapping("/shop/saleList") 
	public ResponseEntity<List<ArtworkDto>>saleList(
			@RequestParam(value = "categoryName", required = false)String category,
			@RequestParam(value = "typeId", required = false)String type,
			@RequestParam(value = "subjectId", required = false)String subject,
			@RequestParam(value = "keyword", required = false)String keyword,
			@RequestParam(value = "saleStatus", required = false)String saleStatus,
			@RequestParam(value="page", required = false, defaultValue = "1")Integer page,
			@RequestParam(value ="size", required = false, defaultValue = "0") int size){
		try {
			System.out.println(page+ "page1");
			System.out.println(category + "category1");
			System.out.println(type + "type");
			System.out.println(subject+ "subject");
			System.out.println(keyword +"keyword");
			System.out.println(saleStatus +"saleStatus");
			System.out.println(size +"size");
			
			List<ArtworkDto> artworkList = shopService.artworkList(page, category, keyword, type, subject, saleStatus,size);
			System.out.println(artworkList);
			return new ResponseEntity<List<ArtworkDto>>(artworkList, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ArtworkDto>>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/shop/saleList/{artwork_name}")
	public ResponseEntity<Map<String,Object>>saleList(@RequestParam(value="page", required = false, defaultValue = "1")Integer page,
			@RequestParam(value = "keyword", required = false)String keyword){
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<ArtworkDto> artworkList = shopService.artworkListByArtist(pageInfo,keyword);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("artworkList", artworkList);
			listInfo.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String,Object>>(listInfo, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
}
