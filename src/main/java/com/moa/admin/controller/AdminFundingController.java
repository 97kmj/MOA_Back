package com.moa.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moa.admin.dto.FundingApplyDto;
import com.moa.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminFundingController {
	private final AdminService adminService;
	
	@GetMapping("/adminFundings")
	public ResponseEntity<List<FundingApplyDto>> adminFundings() throws Exception {
		try {
			List<FundingApplyDto> applyList = adminService.getApplyFundingList();
			return new ResponseEntity<List<FundingApplyDto>>(applyList,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<FundingApplyDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/approveFunding")
	public ResponseEntity<String> approveFunding(@RequestBody Map<String,Long> reqBody) throws Exception {
		try {
			adminService.approveFunding(reqBody.get("fundingId"));
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/rejectFunding")
	public ResponseEntity<String> rejectFunding(@RequestBody Map<String,Long> reqBody) throws Exception {
		try {
			adminService.rejectFunding(reqBody.get("fundingId"));
			return new ResponseEntity<String>(String.valueOf(true),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(String.valueOf(false),HttpStatus.BAD_REQUEST);
		}
	}
}
