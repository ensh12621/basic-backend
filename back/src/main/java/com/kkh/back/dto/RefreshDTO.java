package com.kkh.back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshDTO {

	@NotBlank(message = "토큰 값은 필수입니다.")
	private String refreshToken;
}
