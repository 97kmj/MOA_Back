package com.moa.user.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moa.config.auth.PrincipalDetails;
import com.moa.config.jwt.JwtToken;
import com.moa.entity.User;
import com.moa.repository.UserRepository;
import com.moa.user.dto.RegisterRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        JwtToken jwtToken,
        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtToken = jwtToken;
        this.authenticationManager = authenticationManager;
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        Boolean check = userRepository.findByUsername(username.trim()).isEmpty();
        return ResponseEntity.ok(check); // true = 사용 가능, false = 중복
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

    
}
