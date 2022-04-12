package com.jy.config.auth;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jy.domain.user.User;
import com.jy.domain.user.UserRepository;
import com.jy.web.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	private final HttpSession session;
	
	/* username 이 DB에 존재하는지 확인 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() ->
													new UsernameNotFoundException("사용자가 존재하지 않습니다. : "+username));
		// 세션에 값 저장
		session.setAttribute("user", new UserDto.ResponseUserDto(user));
		
		/* 시큐리티 세션에 유저 정보 저장 */
		return new CustomUserDetails(user);
	}
		

}
