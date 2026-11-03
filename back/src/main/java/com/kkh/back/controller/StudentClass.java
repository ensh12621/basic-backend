package com.kkh.back.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkh.back.dto.StudentDTO;

import jakarta.validation.Valid;

@RequestMapping("/student")
@RestController 
public class StudentClass {

	
	@RequestMapping("/add")
	public ResponseEntity<?> addStudent(@RequestBody @Valid StudentDTO dto){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap.put("result", "success");
		
		return ResponseEntity.ok(resultMap);
		
		
	}
}
