package com.moa.config.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

	private final Map<String, Object> attributes;
	private Map<String, Object> properties;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		properties = (Map<String, Object>)attributes.get("properties");
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
		return (String)properties.get("email");
	}

	@Override
	public String getName() {
		return (String)properties.get("nickname");
	}

	@Override
	public String getProfileImage() {
		return (String)properties.get("profile_image");
	}

	@Override
	public String getNickname() {
		return (String)properties.get("nickname");
	}

	@Override
	public String getMobile() {
		return (String)properties.get("mobile");
	}
}
