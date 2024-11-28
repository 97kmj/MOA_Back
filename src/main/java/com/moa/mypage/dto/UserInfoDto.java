package com.moa.mypage.dto;

import com.moa.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private String name;            // 사용자 이름
    private String username;        // 사용자 아이디
    private String password;        // 사용자 비밀번호
    private String phone;           // 휴대폰 번호
    private String postcode;        // 우편번호
    private String address;         // 기본 주소
    private String detailAddress;   // 상세 주소
    private String extraAddress;    // 참고 주소
    private String email;           // 이메일
    private User.Role role;

    // 엔티티 -> DTO 변환 메서드
    public static UserInfoDto fromEntity(User userInfo) {
        return UserInfoDto.builder()
            .name(userInfo.getName())
            .username(userInfo.getUsername())
            .password(userInfo.getPassword())
            .phone(userInfo.getPhone())
            .postcode(userInfo.getPostcode())
            .address(userInfo.getAddress())
            .detailAddress(userInfo.getDetailAddress())
            .extraAddress(userInfo.getExtraAddress())
            .email(userInfo.getEmail())
            .role(userInfo.getRole())
            .build();
    }

}
