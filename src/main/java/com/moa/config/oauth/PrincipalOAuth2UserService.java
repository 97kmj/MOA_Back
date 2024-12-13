package com.moa.config.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.moa.config.auth.PrincipalDetails;
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
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인");
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버 로그인");
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("카카오와 네이버만 지원합니다.");
		}
		
		//1. DB에서 조회
		Optional<User> ouser = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());
		User user = null;
		//1-1. 이미 가입되어 있으면 정보 수정
		if(ouser.isPresent()) {
			user = ouser.get();
			user.setNickname(oAuth2UserInfo.getNickname());
//			user.setEmail(oAuth2UserInfo.getEmail());
//			user.setPhone(oAuth2UserInfo.getMobile());
			user.setProfileImage(oAuth2UserInfo.getProfileImage());
			userRepository.save(user);
		} else { //1-2. 가입되어 있지 않으면 삽입
			user = User.builder()
							.username(oAuth2UserInfo.getProviderId())
							.name(oAuth2UserInfo.getName())
							.nickname(oAuth2UserInfo.getNickname())
							.password("SOCIAL_LOGIN")
							.email(oAuth2UserInfo.getEmail())
							.phone(oAuth2UserInfo.getMobile())
							.profileImage(oAuth2UserInfo.getProfileImage())
							.role(User.Role.USER)
							.provider(oAuth2UserInfo.getProvider())
							.providerId(oAuth2UserInfo.getProviderId())
							.build();
			userRepository.save(user);
		}
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}


