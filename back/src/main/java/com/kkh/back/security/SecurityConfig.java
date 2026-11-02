package com.kkh.back.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 설정 (REST API 서버이거나 프론트엔드와 분리된 경우 보통 disable 처리)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfiguraitonSource()))
            // 2. HTTP 요청에 대한 권한 설정 (인증/인가)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // 3. 폼 로그인 설정 (기본 로그인 페이지 사용 예시)
            .formLogin(form -> form.disable())

            // 4. 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 세션 초기화
            );

        return http.build();
    }

    private CorsConfigurationSource corsConfiguraitonSource() {
    	CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 프론트엔드 오리진(도메인) 설정
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:3000", "http://localhost:3000")); // localhost도 함께 허용하면 유용합니다.
        
        // 허용할 HTTP Method 설정 (GET, POST, PUT, DELETE 등)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // 허용할 HTTP Header 설정
        configuration.setAllowedHeaders(List.of("*"));
        
        // 쿠키나 인증 토큰(Authorization Header 등)을 포함한 요청을 허용할지 여부
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로("/**")에 대해 위 CORS 정책을 적용
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}