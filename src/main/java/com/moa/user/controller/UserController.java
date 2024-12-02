package com.moa.user.controller;

import com.moa.config.auth.PrincipalDetails;
import com.moa.config.jwt.JwtToken;
import com.moa.entity.User;
import com.moa.entity.User.ApprovalStatus;
import com.moa.user.dto.RegisterRequest;
import com.moa.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

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

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        System.out.println("Username check: " + username + " exists: " + exists); // 디버깅용 로그
        return ResponseEntity.ok(!exists); // true = 사용 가능, false = 중복
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // 사용자 검증
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }

        User user = optionalUser.get();

        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }

        // JWT 토큰 생성
        String accessToken = jwtToken.makeAccessToken(username);
        String refreshToken = jwtToken.makeRefreshToken(username);

        // 사용자 데이터 생성
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("nickname", user.getNickname());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("artistApprovalStatus", user.getArtistApprovalStatus());
        userData.put("role", user.getRole());
        userData.put("phone", user.getPhone());
        userData.put("address", user.getAddress());

        // 응답 데이터 설정 (JWT는 헤더로 전달)
        return ResponseEntity.ok()
            .header("Authorization", "Bearer " + accessToken)
            .header("Refresh-Token", "Bearer " + refreshToken)
            .body(userData);
    }


    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            // 비밀번호 암호화
            String rawPassword = request.getPassword();
            String encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

            // User 엔티티 생성 및 저장
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(encodedPassword);
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPostcode(request.getPostcode());
            user.setAddress(request.getAddress());
            user.setDetailAddress(request.getDetailAddress());
            user.setExtraAddress(request.getExtraAddress());
            user.setArtistApprovalStatus(User.ApprovalStatus.NORMAL);
            user.setRole(User.Role.USER);

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + e.getMessage());
        }
    }

    // 현재 로그인한 사용자 정보 가져오기
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return ResponseEntity.ok(user);
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
