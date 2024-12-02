package com.moa.user.dto;

import com.moa.entity.User;
import com.moa.shop.dto.ArtworkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUserInfoDto {
	private String username;
	private String name;
	private String email;
    private String phone;
    private String address;
    private String detailAddress;
    private String extraAddress;


public static OrderUserInfoDto fromUserInfo(User user) {
	OrderUserInfoDto orderUserInfoDto = OrderUserInfoDto.builder()
			.username(user.getUsername())
			.name(user.getName())
			.email(user.getEmail())
			.phone(user.getPhone())
			.address(user.getAddress())
			.detailAddress(user.getDetailAddress())
			.extraAddress(user.getExtraAddress())
			.build();
	return orderUserInfoDto;
	}
}


