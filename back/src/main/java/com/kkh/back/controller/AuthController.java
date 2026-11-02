package com.kkh.back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kkh.back.service.AuthService;

@RequestMapping("/auth")
@RestController
public class AuthController {

	
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
	
}
