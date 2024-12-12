package com.moa.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResponseDto {
	private Boolean verified;
	private String message;
	private String username;
	private String type; //회원가입:join, 아이디찾기:search 
}