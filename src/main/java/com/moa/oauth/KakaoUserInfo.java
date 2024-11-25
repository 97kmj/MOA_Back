package com.moa.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "Kakao"; // OAuth2 제공자 이름
    }


@Override
public String getEmail() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

    if (kakaoAccount == null) {
        System.out.println("kakao_account is null");
        return null;
    }
    System.out.println("has_email: " + kakaoAccount.get("has_email"));
    System.out.println("email_needs_agreement: " + kakaoAccount.get("email_needs_agreement"));

    // 이메일 정보를 가져옴
    if (Boolean.TRUE.equals(kakaoAccount.get("has_email"))
        && Boolean.FALSE.equals(kakaoAccount.get("email_needs_agreement"))) {
        return (String) kakaoAccount.get("email");
    }

    return null; // 이메일 정보가 없거나 동의가 필요한 경우
}


    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                return (String) profile.get("nickname"); // 닉네임 가져오기
            }
        }
        return null;
    }
}
