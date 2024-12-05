package com.moa.admin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.AdminOrderItemDto;
import com.moa.admin.dto.UpdateOrderItemStatusRequest;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminItemController {
	private final AdminService adminService;
	
	@GetMapping("/adminItem")
	public ResponseEntity<List<AdminOrderItemDto>> adminItem() throws Exception{
		try {
			List<AdminOrderItemDto> itemList = adminService.getOrderItemList();
			return new ResponseEntity<List<AdminOrderItemDto>>(itemList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<AdminOrderItemDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/adminItem")
	public ResponseEntity<String> updateOrderItemStatus(@RequestBody List<UpdateOrderItemStatusRequest> updates) throws Exception {
		try {
			adminService.updateShippingStatus(updates);
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
			
		}
	}
	
}
