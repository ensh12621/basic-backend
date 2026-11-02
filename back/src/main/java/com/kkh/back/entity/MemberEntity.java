package com.kkh.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="member")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {

	@Id
	private String userPk;
	
	@Column(nullable = false, unique = true)
	private String userId;
	
	
	@Column(nullable = false)
	private String password;
	
	private String nickname;
	
	private String email;
	
}
