package com.jy.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	/* security */
	Optional<User> findByUsername(String username);
	
	User findByNickname(String nickname);
	
	/* 유효성 검사 - 중복 체크
	 * 중복 : true
	 * 중복이 아닌 경우 : false
	 */
	boolean existsByUsername(String username);
	boolean existsByNickname(String nickname);
	boolean existsByEmail(String email);
}
