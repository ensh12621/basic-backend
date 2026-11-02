package com.kkh.back.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kkh.back.dto.JoinDTO;
import com.kkh.back.entity.MemberEntity;
import com.kkh.back.repository.MemberRepository;

@Service
public class AuthServiceImpl implements AuthService{

	private MemberRepository memberRepository;
	private PasswordEncoder passwordEncoder;
	
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

}
