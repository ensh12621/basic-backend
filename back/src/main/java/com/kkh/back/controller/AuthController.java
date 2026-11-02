package com.kkh.back.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkh.back.dto.JoinDTO;
import com.kkh.back.service.AuthService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	private AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/dup-check-id")
	public ResponseEntity<Map<String, Object>> duplicationCheckId(@RequestParam("userId") String userId){
		Map<String, Object> map = new HashMap<>();
		boolean duplicated = authService.checkUserIdDuplicated(userId);
		map.put("duplicated", duplicated);
		return ResponseEntity.ok(map);
		
	}
	
	
	@PostMapping("/join-user")
	public ResponseEntity<Map<String, Object>> joinUser(@RequestBody @Valid JoinDTO dto){
		logger.info(dto.toString());
		boolean result = authService.addMember(dto);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("result", result);
		return ResponseEntity.ok(resultMap);
		
	}
}
