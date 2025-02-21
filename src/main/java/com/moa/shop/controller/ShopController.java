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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moa.admin.dto.FrameDto;
import com.moa.entity.Artwork;
import com.moa.shop.dto.ArtworkDto;
import com.moa.shop.dto.CanvasDto;
import com.moa.shop.dto.CategoryDto;
import com.moa.shop.dto.OrderRequest;
import com.moa.shop.dto.SubjectDto;
import com.moa.shop.dto.TypeDto;
import com.moa.shop.service.ShopService;
import com.moa.user.dto.OrderUserInfoDto;
import com.moa.user.service.LikeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/shop")
@Slf4j
public class ShopController {
	
	private static final int ResponseEntity = 0;
	private static final int Boolean = 0;
	@Autowired
	private ShopService shopService;
    @Autowired
    private LikeService likeService;
    
	@GetMapping("/artworkAdd")
	public ResponseEntity<List<CategoryDto>> categoryList(){
		try {
			List<CategoryDto> categoryList = shopService.categoryList();
			return new ResponseEntity<List<CategoryDto>>(categoryList, HttpStatus.OK); 
			
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<List<CategoryDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/getFrame/{canvasId}")
	public ResponseEntity<List<FrameDto>> FrameListByCanvasId (@PathVariable("canvasId") Long canvasId ){
		try {
			

		List<FrameDto> frameList = shopService.frameListByCanvasId(canvasId);

			return new ResponseEntity<List<FrameDto>>(frameList, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<FrameDto>>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/artworkAdd/canvas/{canvasType}")
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
	
	@PostMapping("/artworkAdd/type/{categoryId}")
	public ResponseEntity<List<TypeDto>> typeListByCategory(@PathVariable Integer categoryId) {
        try {
        	List<TypeDto> typeList = shopService.typeList(categoryId);
        	return new ResponseEntity<List<TypeDto>>(typeList, HttpStatus.OK);
        }catch(Exception e){
        	e.printStackTrace();
			return new ResponseEntity<List<TypeDto>>(HttpStatus.BAD_REQUEST);
        }
        
    }
	@PostMapping("/artworkAdd/subject/{categoryId}")
	public ResponseEntity<List<SubjectDto>> subjectListByCategory(@PathVariable Integer categoryId) {
        try {
        	List<SubjectDto> subjectList = shopService.subjectList(categoryId);
        	return new ResponseEntity<List<SubjectDto>>(subjectList, HttpStatus.OK);
        }catch(Exception e){
        	e.printStackTrace();
			return new ResponseEntity<List<SubjectDto>>(HttpStatus.BAD_REQUEST);
        } 
    }

	@GetMapping("/saleList") 
	public ResponseEntity<Map<String,Object>>saleList(
			@RequestParam(value = "categoryId", required = false) Integer category,
			@RequestParam(value = "typeId", required = false) Integer type,
			@RequestParam(value = "subjectId", required = false) Integer subject,
			@RequestParam(value = "searchKeyword", required = false)String keyword,
			@RequestParam(value = "saleStatus", required = false) Artwork.SaleStatus saleStatus,
			@RequestParam(value="page", required = false, defaultValue = "0")Integer page,
			@RequestParam(value ="size", required = false, defaultValue = "8") int size){
		try {
			Map<String,Object> artworks = shopService.artworkList(page, category, keyword, type, subject, saleStatus,size);
			return new ResponseEntity<>(artworks, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// detail 가가져오기
	
	@GetMapping("/artworkDetail/{artworkId}")
	public ResponseEntity<ArtworkDto> artworkDetail(@PathVariable Long artworkId){
		try {
			return new ResponseEntity<ArtworkDto>(shopService.artworkDetail(artworkId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ArtworkDto>(HttpStatus.BAD_REQUEST);
		}
	}	  
	
	@PostMapping("/isLikeArtwork/{artworkId}")
	public ResponseEntity<Boolean> isLikeArtwork(@PathVariable Long artworkId, @RequestBody Map<String,String> param){
		try {
			
			return new ResponseEntity<Boolean>(shopService.isLikeArtwork(param.get("username"), artworkId), HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}	  
	
	@PostMapping("/likeArtwork")
	public ResponseEntity<Boolean> likeArtwork(@RequestBody Map<String,String> param){
		try {
			Boolean isLike = shopService.toggleLikeArtwork(param.get("username"), Long.valueOf(param.get("artworkId")));
			return new ResponseEntity<Boolean>(isLike, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/orderData")
	public ResponseEntity<Map<String,Object>> OrderArtwork(@RequestBody OrderRequest orderRequest){
		try {
		    Long artworkId = orderRequest.getArtworkId();
		    String username = orderRequest.getUsername();
		    Long frameId = orderRequest.getFrameId();
			ArtworkDto artworkList= shopService.artworkDetail(artworkId);
			OrderUserInfoDto UserList = shopService.orderUserInfo(username);
			
			Map<String,Object> orderInfo = new HashMap<>();
			orderInfo.put("artworkList", artworkList);
			orderInfo.put("userList", UserList);

			if (frameId != null) {

	            List<FrameDto> frameList = shopService.frameList(frameId);
	            orderInfo.put("frameList", frameList);
	        } else {
	        	orderInfo.put("frameList", null);
	        }
				
			return new ResponseEntity<Map<String,Object>>(orderInfo, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String,Object>>(HttpStatus.BAD_REQUEST);
			
		}
	}
	

	
	
}