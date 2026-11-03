package com.kkh.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
	
	@NotBlank(message = "학생이름은 필수 입력값입니다.")
	private String studentName;
	
	@Email(message = "이메일 형식이 유효하지 않습니다.")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	private String email;
	
	@NotBlank(message = "성별은 필수 입력값입니다.")
	private String gender;
	
	@NotBlank(message = "학과코드는 필수 입력값입니다.")
	private String deptNo;
	
}
