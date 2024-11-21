package com.moa.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.moa.entity.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private final User user;
    private final Map<String, Object> attributes;

    // 기본 생성자 (소셜 로그인이 아닌 일반 로그인)
    public PrincipalDetails(User user) {
        this.user = user;
        this.attributes = null;
    }

    // 소셜 로그인용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> user.getRole().name()); // Enum 타입의 Role 사용
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 추가적인 만료 로직이 필요하면 변경 가능
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 추가적인 잠금 로직이 필요하면 변경 가능
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 추가적인 자격 증명 만료 로직이 필요하면 변경 가능
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용자 활성화 여부에 따라 변경 가능
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // OAuth2User에서 제공하는 메서드
    }

    @Override
    public String getName() {
        return user.getUsername(); // OAuth2User에서 제공하는 메서드, 사용자 ID로 대체
    }
}
