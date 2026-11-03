package com.kkh.back.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kkh.back.dto.JoinDTO;
import com.kkh.back.dto.LoginDTO;
import com.kkh.back.entity.MemberEntity;
import com.kkh.back.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthServiceImpl implements AuthService{

	private MemberRepository memberRepository;
	private PasswordEncoder passwordEncoder;

	private final String SECRET_KEY = "your-very-secure-and-long-secret-key-that-is-at-least-256-bits";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	public AuthServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public boolean checkUserIdDuplicated(String userId) {
		logger.info("userId = {}", userId);
		MemberEntity entity = memberRepository.findByUserId(userId);
		return entity != null;
	}

	@Override
	public boolean addMember(JoinDTO dto) {
		UUID uuid = UUID.randomUUID();
		MemberEntity entity = MemberEntity.builder()
								.userPk(uuid.toString())
								.userId(dto.getUserId())
								.password(passwordEncoder.encode(dto.getUserPwd()))
								.nickname(dto.getNickname())
								.email(dto.getEmail())
								.build();
		return memberRepository.save(entity) != null;
	}

	@Override
	public Map<String, Object> login(LoginDTO dto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		MemberEntity memberEntity = memberRepository.findByUserId(dto.getUserId());
		if(memberEntity == null) {
			resultMap.put("result", "failed");
			return resultMap;
		}
		
		
		boolean matched = passwordEncoder.matches(dto.getUserPwd(), memberEntity.getPassword());
		
		if(!matched) {
			resultMap.put("result", "failed");
			return resultMap;
		}
		
		String accessToken = createJwtToken(dto.getUserId(), ACCESS_TOKEN_VALIDITY);
		String refreshToken = createJwtToken(dto.getUserId(), REFRESH_TOKEN_VALIDITY);
		
		resultMap.put("result", "success");
		resultMap.put("accessToken", accessToken);
		resultMap.put("refreshToken", refreshToken);
		
		return resultMap;
	}

	private String createJwtToken(String userId, long period) {
		Date now = new Date();
        Date validity = new Date(now.getTime() + period);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();	
	}

	@Override
	public String refreshToken(String refreshToken) {
		
		String userId = getSubjectFromJwt(refreshToken);
		
		String newAccessToken = createJwtToken(userId, ACCESS_TOKEN_VALIDITY);
		logger.info("새로 발급받은 액세스토큰 => "+ newAccessToken);
		
		return newAccessToken;
	}

	private String getSubjectFromJwt(String refreshToken) {
		try {
            // 토큰을 파싱하여 Claims(페이로드) 객체 추출
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // 토큰 생성 시 사용한 key와 동일해야 함
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Subject에 담겨 있던 유저 아이디(userId) 반환
            return claims.getSubject();
            
        } catch (Exception e) {
            // 토큰이 만료되었거나 위조된 경우 예외 발생
            logger.error("JWT 토큰 파싱 중 오류 발생: {}", e.getMessage());
            return null;
        }
	}
	

}
