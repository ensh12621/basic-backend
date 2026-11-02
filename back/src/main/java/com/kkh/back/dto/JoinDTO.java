package com.kkh.back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinDTO {

	@Size(min=6, message="유저 아이디는 최소 6글자 이상이어야 합니다.")
	@NotBlank(message = "유저 아이디는 필수값입니다.")
	private String userId;
	
	@Size(min=6, message ="유저 비밀번호는 최소 6글자 이상이어야 합니다.")
	@NotBlank(message = "비밀번호는 필수값입니다.")
	private String userPwd;
	
	@Size(min=2, message = "닉네임은 최소 2글자 이상이어야 합니다.")
	@NotBlank(message = "닉네임은 필수값입니다,")
	private String nickname;
	
	@Email(message = "이메일 형식이 유효하지 않습니다")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	private String email;
}
