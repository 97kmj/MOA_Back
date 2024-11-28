package com.moa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		// CORS 설정 소스 생성
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// CORS 설정
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowCredentials(true); // 쿠키를 포함한 인증 정보 허용
		config.addAllowedOriginPattern("*"); // 모든 도메인 허용
		config.addAllowedHeader("*"); // 모든 헤더 허용
		config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
		config.addExposedHeader("Authorization"); // 클라이언트가 응답 헤더를 읽을 수 있도록 설정

		// 특정 엔드포인트에 대해 CORS 설정 적용
		source.registerCorsConfiguration("/**", config);
		
		return new CorsFilter(source);
	}
}
