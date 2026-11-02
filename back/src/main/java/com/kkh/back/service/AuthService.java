package com.kkh.back.service;

import com.kkh.back.dto.JoinDTO;

public interface AuthService {

	boolean checkUserIdDuplicated(String userId);

	boolean addMember(JoinDTO dto);

}
