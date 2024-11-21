package com.moa.oauth;

import com.moa.config.auth.PrincipalDetails;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.moa.oauth.KakaoUserInfo;
import com.moa.oauth.NaverUserInfo;
import com.moa.oauth.OAuth2UserInfo;
import com.moa.entity.User;
import com.moa.repository.UserRepository;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public PrincipalOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 디버깅: 가져온 속성을 로그로 출력
        System.out.println("OAuth2User Attributes: " + oAuth2User.getAttributes());

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        }

        // 1. DB에서 사용자 조회
        User user = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId())
            .orElseGet(() -> registerNewUser(oAuth2UserInfo));

        // 2. 기존 사용자 정보 업데이트
        updateUser(user, oAuth2UserInfo);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return new KakaoUserInfo(attributes);
        } else if ("naver".equals(registrationId)) {
            return new NaverUserInfo(attributes);
        } else {
            return null;
        }
    }

    private void updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setEmail(oAuth2UserInfo.getEmail());
        userRepository.save(user);
    }

    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();

        // 이메일이 null이거나 빈 값일 경우 기본 이메일 설정
        if (email == null || email.isEmpty()) {
            email = "noemail_" + oAuth2UserInfo.getProviderId() + "@default.com";
        }
        String name = oAuth2UserInfo.getName();

        // 이름이 null이거나 빈 값일 경우 기본 이름 설정
        if (name == null || name.isEmpty()) {
            name = "User_" + oAuth2UserInfo.getProviderId();
        }

        User newUser = User.builder()
            .username(oAuth2UserInfo.getProviderId())
            .email(email)
            .name(name) // nickname 저장
            .password("SOCIAL_LOGIN")
            .role(User.Role.USER) // Enum 기반의 Role 설정
            .provider(oAuth2UserInfo.getProvider())
            .providerId(oAuth2UserInfo.getProviderId())
            .build();
        return userRepository.save(newUser);
    }
}


