package com.moa.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequest {
	private String imp_key; // API 키
	private String imp_secret; // API Secret
}