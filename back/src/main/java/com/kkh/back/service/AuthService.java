package com.kkh.back.service;

import java.util.Map;

import com.kkh.back.dto.JoinDTO;
import com.kkh.back.dto.LoginDTO;

public interface AuthService {
	
// 	Access Token 유효기간 (예: 1분)
    public static final long ACCESS_TOKEN_VALIDITY = 1000L * 60;
    // Refresh Token 유효기간 (예: 2주)
    public static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 14;

	boolean checkUserIdDuplicated(String userId);

	boolean addMember(JoinDTO dto);

	Map<String, Object> login(LoginDTO dto);

}
