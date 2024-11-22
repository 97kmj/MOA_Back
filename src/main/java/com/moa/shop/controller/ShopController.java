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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	@GetMapping("shop/artworkAdd/canvas")
	public ResponseEntity<List<CanvasDto>> CanvasList(){
		try {
			List<CanvasDto> canvasList = shopService.canvasList();
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
	
	
	
	
	
	@PostMapping("/shop/artworkAdd")
	public ResponseEntity<Long> artworkAdd(ArtworkDto artworkDto,
		@RequestParam(name="file",required = false) MultipartFile[] file){
			try {
				Long artworkNum = shopService.artworkAdd(artworkDto, file);
				return new ResponseEntity<Long>(artworkNum, HttpStatus.OK);
			}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Long>(HttpStatus.BAD_REQUEST);
			}
		}
	
	
	
	@GetMapping("/shop/saleList")
	public ResponseEntity<Map<String,Object>>saleList(@RequestParam(value="page", required = false, defaultValue = "1")Integer page,
			@RequestParam(value = "category", required = false)String category,
			@RequestParam(value = "type", required = false)String type,
			@RequestParam(value = "subject", required = false)String subject,
			@RequestParam(value = "keyword", required = false)String keyword,
			@RequestParam(value = "saletype", required = false)String saletype){
		try {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<ArtworkDto> artworkList = shopService.artworkList(pageInfo, category, type, subject, saletype);
			Map<String, Object> listInfo = new HashMap<>();
			listInfo.put("artworkList", artworkList);
			listInfo.put("pageInfo", pageInfo);
			return new ResponseEntity<Map<String,Object>>(listInfo, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
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
