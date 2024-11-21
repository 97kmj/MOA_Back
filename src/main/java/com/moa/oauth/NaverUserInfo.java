package com.moa.oauth;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        // 네이버 API의 "response" 객체를 기준으로 초기화
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return attributes != null ? (String) attributes.get("id") : null;
    }

    @Override
    public String getProvider() {
        return "Naver"; // OAuth2 제공자 이름
    }

    @Override
    public String getEmail() {
        return attributes != null ? (String) attributes.get("email") : null;
    }

    @Override
    public String getName() {
        return attributes != null ? (String) attributes.get("name") : null;
    }
}
