package com.moa.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moa.config.auth.PrincipalDetails;
import com.moa.entity.User;
import com.moa.repository.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtToken jwtToken) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // 등록 및 로그인 관련 경로는 필터를 적용하지 않음
        String method = request.getMethod();

        // OPTIONS 요청 제외
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        return
            path.equals("/api/user/check-username") ||
            path.equals("/api/user/register") ||
            path.equals("/api/user/login") ||
            path.equals("/api/verification/send-email") ||
            path.equals("/api/verification/send-sms") ||
            path.equals("/api/verification/verify-email") ||
            path.equals("/api/verification/verify-sms") ||
            path.equals("/api/verification/confirmId") ||
            path.equals("/api/verification/changePassword") ||
            path.equals("/main") ||
			path.equals("/api/artworks") ||
			path.startsWith("/api/artworks/") ||
			path.startsWith("/api/phone/") ||
            path.startsWith("/oauth2/") ||
            path.startsWith("/shop/") ||
            path.startsWith("/artistDetail/") ||
            path.equals("/galleryDetail") ||
            path.equals("/artistArtworks") ||
            path.equals("/notice") ||
            path.equals("/cart") ||
			path.startsWith("/api/sse")
            ||(path.equals("/api/funding") && "GET".equalsIgnoreCase(method)) ||
				(path.matches("^/api/funding/\\d+$") && "GET".equalsIgnoreCase(method));

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        String uri = request.getRequestURI();
//        System.out.println("Request URI: " + uri);


		String authentication = request.getHeader(JwtProperties.HEADER_STRING);
		if (authentication == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> token = objectMapper.readValue(authentication, Map.class);

		//accessToken : Header로 부터 accessToken 가져와 bearer 체크 
		String accessToken = token.get("access_token");
		if (!accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
			return;
		}

		accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");
		try {
			//1. Access Token check
			//1-1. 보안키, 만료시간 check
			String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
				.build()
				.verify(accessToken)
				.getClaim("sub")
				.asString();

			//1-2. username check
			if (username == null || username.equals(""))
				throw new Exception(); // 사용자가 없을 때
			Optional<User> ouser = userRepository.findById(username);
			if (ouser.isEmpty())
				throw new Exception();
			User user = ouser.get();

			if (user == null)
				throw new Exception(); // 사용자가 DB에 없을 때
			//1-3. User 를 Authentication로 생성하여 Security Session에 넣어준다. (그러면 COntroller에서 사용할 수 있다.)
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalDetails,
				null,
				principalDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
			chain.doFilter(request, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				//2. Refresh Token check : Access Token invalidate일 경우 
				String refreshToken = token.get("refresh_token");
				if (!refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
					return;
				}
				refreshToken = refreshToken.replace(JwtProperties.TOKEN_PREFIX, "");
				//2-1. 보안키, 만료시간 check
				String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
					.build()
					.verify(refreshToken)
					.getClaim("sub")
					.asString();
				System.out.println(username);
				//2-2. username check
				if (username == null || username.equals(""))
					throw new Exception(); // 사용자가 없을 때
				Optional<User> ouser = userRepository.findById(username);
				if (ouser.isEmpty())
					throw new Exception();
				User user = ouser.get();

				//accessToken, refreshToken 다시 만들어 보낸다.
				String reAccessToken = jwtToken.makeAccessToken(username);
				String reRefreshToken = jwtToken.makeRefreshToken(username);
				Map<String, String> map = new HashMap<>();
				map.put("access_token", JwtProperties.TOKEN_PREFIX + reAccessToken);
				map.put("refresh_token", JwtProperties.TOKEN_PREFIX + reRefreshToken);
				String reToken = objectMapper.writeValueAsString(map); // map -> jsonString
				response.addHeader(JwtProperties.HEADER_STRING, reToken);
				response.setContentType("application/json; charset=utf-8");
				//response.getWriter().print("token");				
				chain.doFilter(request, response);  // 헤더에 토큰 갱신 후 다시 컨트롤러에 요청
			} catch (Exception e2) {
				e2.printStackTrace();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
			}
		}
	}
}

