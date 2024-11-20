package com.moa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//import com.moa.config.jwt.JwtProperties;

@Configuration
public class CorsConfig {
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
//		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");  //Access-Control-Allow-Headers
		config.addAllowedMethod("*");  //Access-Control-Allow-Method
//		config.addExposedHeader(JwtProperties.HEADER_STRING); // 클라이언트 (리액트 등)가 응답에 접근할 수 있는 Header 추가 
		source.registerCorsConfiguration("/*", config);
		source.registerCorsConfiguration("/*/*", config);
		source.registerCorsConfiguration("/*/*/*", config);
		return new CorsFilter(source);
	}
}
