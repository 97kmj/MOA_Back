package com.moa.mypage.service;

import com.moa.mypage.dto.UserInfoDto;

public interface UserInfoService {
    UserInfoDto getUserInfo(String username); // 사용자 정보 조회
    void updateUserInfo(UserInfoDto userInfoDto); // 사용자 정보 업데이트
}
