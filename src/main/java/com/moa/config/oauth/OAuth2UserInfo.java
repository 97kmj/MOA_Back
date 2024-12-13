package com.moa.config.oauth;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getNickname();
    String getName();
    String getProfileImage();
    String getMobile();
}
