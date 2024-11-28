package com.moa.mypage.service;

import com.moa.entity.User;
import com.moa.mypage.dto.UserInfoDto;
import com.moa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    @Autowired
    public UserInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserInfoDto getUserInfo(String username) {
        // 사용자 정보 조회
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        // 엔티티 -> DTO 변환
        return UserInfoDto.fromEntity(user);
    }

    @Override
    public void updateUserInfo(UserInfoDto userInfoDto) {
        User user = userRepository.findByUsername(userInfoDto.getUsername())
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (userInfoDto.getPassword() != null && !userInfoDto.getPassword().isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(userInfoDto.getPassword())); // 비밀번호 암호화
        }
        if (userInfoDto.getPhone() != null) {
            user.setPhone(userInfoDto.getPhone());
        }

        if (userInfoDto.getPhone() != null) {
            user.setPhone(userInfoDto.getPhone());
        }
        if (userInfoDto.getPostcode() != null) {
            user.setPostcode(userInfoDto.getPostcode());
        }
        if (userInfoDto.getAddress() != null) {
            user.setAddress(userInfoDto.getAddress());
        }
        if (userInfoDto.getDetailAddress() != null) {
            user.setDetailAddress(userInfoDto.getDetailAddress());
        }
        if (userInfoDto.getExtraAddress() != null) {
            user.setExtraAddress(userInfoDto.getExtraAddress());
        }
        if (userInfoDto.getEmail() != null) {
            user.setEmail(userInfoDto.getEmail());
        }
        if (userInfoDto.getRole() != null) {
            user.setRole(userInfoDto.getRole());
        }

        userRepository.save(user);
    }

}
