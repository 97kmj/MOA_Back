package com.moa.user.controller;

import com.moa.config.auth.PrincipalDetails;
import com.moa.config.jwt.JwtToken;
import com.moa.entity.User;
import com.moa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtToken jwtToken;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtToken jwtToken) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtToken = jwtToken;
    }

    // 현재 로그인한 사용자 정보 가져오기
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return ResponseEntity.ok(user);
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // 비밀번호 암호화
        String rawPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        // 기본 권한 설정
        user.setRole(User.Role.USER);

        // 저장
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    // 아티스트 승격
    @PostMapping("/promote-to-artist/{username}")
    public ResponseEntity<String> promoteToArtist(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(User.Role.ARTIST); // Role을 ARTIST로 변경
            userRepository.save(user);
            return ResponseEntity.ok("User promoted to Artist.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // JWT 토큰 재발급
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        // Bearer 토큰에서 "Bearer " 제거
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        // 토큰 검증
        if (!jwtToken.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token.");
        }

        // 사용자 정보 추출
        String username = jwtToken.getUsernameFromToken(refreshToken);

        // 새로운 Access Token 생성
        String newAccessToken = jwtToken.makeAccessToken(username);
        return ResponseEntity.ok(newAccessToken);
    }
}
