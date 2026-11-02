package com.kkh.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkh.back.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{

	MemberEntity findByUserId(String userId);

}
