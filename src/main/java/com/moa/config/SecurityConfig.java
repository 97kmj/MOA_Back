package com.moa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //IoC 빈(bean) 등록 
@EnableWebSecurity //필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable(); // cross site request forgery attack (크로스사이트 요청 위조 공격)
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //로그인 필수
//			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 로그인 && 권한이 admin이거나 manager 
//			.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER')") // 로그인&&권한이 manager만 허용 
//			.antMatchers("/manager/**").hasAuthority("ROLE_MANAGER")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/login")
			// "/loginProc" 주소가 호출이 되면 시큐러티가 낚아채서 대신 로그인을 진행.
			// 결과적으로 컨트롤러에 따로 "/loginProc"을 구현하지 않는다.
			// 이 로그인 과정에서 필요한 것은 PrincipalDetails를 만들어 주는 것이다.
			.loginProcessingUrl("/loginProc")
			.defaultSuccessUrl("/");
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder encoderPassword() {
		return new BCryptPasswordEncoder();
	}
	
}
