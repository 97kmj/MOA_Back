package com.moa.config.oauth;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        // 네이버 API의 "response" 객체를 기준으로 초기화
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "Naver"; // OAuth2 제공자 이름
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

	@Override
	public String getProfileImage() {
        return (String)attributes.get("profile_image");
	}

	@Override
	public String getNickname() {
        return (String)attributes.get("nickname");
	}
	
	@Override
	public String getMobile() {
		return (String)attributes.get("mobile");
	}	
}
