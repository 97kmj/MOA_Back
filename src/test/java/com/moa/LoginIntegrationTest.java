package com.moa;

import com.moa.entity.User;
import com.moa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // 데이터 초기화
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    @Test
    void testLogin() throws Exception {
        String loginPayload = "{ \"username\": \"testUser\", \"password\": \"password\" }";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andExpect(status().isOk())
            .andExpect(header().exists("Authorization"));
    }

    @Test
    void testAccessProtectedResource() throws Exception {
        String loginPayload = "{ \"username\": \"testUser\", \"password\": \"password\" }";

        // 로그인 요청 및 토큰 획득
        String token = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andReturn()
            .getResponse()
            .getHeader("Authorization");

        // 보호된 리소스 접근
        mockMvc.perform(get("/api/user/profile")
                .header("Authorization", token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("testUser"));
    }
}
