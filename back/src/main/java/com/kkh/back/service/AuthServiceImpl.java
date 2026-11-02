package com.kkh.back.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kkh.back.entity.MemberEntity;
import com.kkh.back.repository.MemberRepository;

@Service
public class AuthServiceImpl implements AuthService{

	private MemberRepository memberRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	public AuthServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Override
	public boolean checkUserIdDuplicated(String userId) {
		logger.info("userId = {}", userId);
		MemberEntity entity = memberRepository.findByUserId(userId);
		return entity != null;
	}

}
