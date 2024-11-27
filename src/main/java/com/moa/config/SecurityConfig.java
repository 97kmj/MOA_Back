package com.moa.config;


import com.moa.config.jwt.JwtAuthenticationFilter;
import com.moa.config.jwt.JwtAuthorizationFilter;
import com.moa.config.jwt.JwtToken;
import com.moa.oauth.OAuth2SuccessHandler;
import com.moa.oauth.PrincipalOAuth2UserService;
import com.moa.repository.UserRepository;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;
    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtToken jwtToken;

    public SecurityConfig(CorsFilter corsFilter, UserRepository userRepository,
        PrincipalOAuth2UserService principalOAuth2UserService,
        OAuth2SuccessHandler oAuth2SuccessHandler, JwtToken jwtToken) {
        this.corsFilter = corsFilter;
        this.userRepository = userRepository;
        this.principalOAuth2UserService = principalOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.jwtToken = jwtToken;
    }

    // AuthenticationManager를 Bean으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder encoderPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        //추가
        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint());


        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(corsFilter)
            .formLogin().disable()
            .httpBasic().disable()
            .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtToken))
            .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtToken))
            .authorizeRequests()


            // 인증이 필요하지 않은 경로 설정

            // main 이나 / 넣어야할수도
            .antMatchers("/api/user/check-username","/api/user/register", "/api/user/login", "/api/user/refresh-token", "/oauth2/**","/api/user/**","/api/mypage/**").permitAll()
            .antMatchers("/user/**").authenticated() // 로그인 필요
            .antMatchers("/api/like/**").authenticated()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/artist/**").hasRole("ARTIST")
            .anyRequest().permitAll()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization")
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(principalOAuth2UserService)
            .and()
            .successHandler(oAuth2SuccessHandler);

        return http.build();
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"로그인이 필요합니다.\"}");
        };
    }
}
