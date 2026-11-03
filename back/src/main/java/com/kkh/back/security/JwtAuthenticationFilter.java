package com.kkh.back.security;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final Key key;

	public JwtAuthenticationFilter(String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. 요청 쿠키에서 "accessToken" 추출
		String accessToken = resolveAccessToken(request);

		if (accessToken != null) {
			try {
				// 2. JWT 토큰 검증 및 페이로드(Claims) 추출
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();

				String userId = claims.getSubject();

				// 3. 토큰이 유효하고 현재 SecurityContext에 인증 정보가 없다면
				if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					// Spring Security가 인식할 수 있는 UserDetails 객체 생성 (권한은 필요에 맞게 확장 가능)
					UserDetails userDetails = new User(userId, "", new ArrayList<>());

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					// 4. SecurityContext에 인증 정보 등록
					SecurityContextHolder.getContext().setAuthentication(authentication);
					logger.info("JWT 검증 완료 - 아이디:[{}] / 토큰: [{}] ", userId, accessToken);
				}
			} catch (Exception e) {
				// 토큰이 만료되었거나 변조된 경우
				logger.error("JWT 검증 실패: {}", e.getMessage());
				// 여기서 예외를 터트리지 않고 넘어가면, 인증이 필요한 엔드포인트에서 스프링 시큐리티가 알아서 401 처리를 합니다.
			}
		}

		// 다음 필터로 진행
		filterChain.doFilter(request, response);
	}

	// HTTP 요청 쿠키 배열에서 accessToken 값만 찾아내는 헬퍼 메서드
	private String resolveAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		// 헤더가 존재하고 "Bearer "로 시작하는지 확인
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7); // "Bearer " 뒷부분(실제 토큰)만 잘라내어 반환
		}

		return null;
	}
}