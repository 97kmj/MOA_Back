package com.moa.mypage.controller;

import com.moa.mypage.dto.UserInfoDto;
import com.moa.mypage.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    // 사용자 정보 조회 API
    @GetMapping("/userinfoedit")
    public UserInfoDto getUserInfo(Principal principal) {
        String username = principal.getName(); // 인증된 사용자 이름 가져오기
        return userInfoService.getUserInfo(username);
    }

    // 사용자 정보 업데이트 API
    @PatchMapping("/userinfoupdate")
    public Map<String, String> updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        System.out.println("Received UserInfoDto: " + userInfoDto);
        userInfoService.updateUserInfo(userInfoDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "사용자 정보가 성공적으로 업데이트되었습니다.");
        return response;
    }

}
