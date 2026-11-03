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
	

}
