package com.jy.service.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jy.domain.user.User;
import com.jy.domain.user.UserRepository;
import com.jy.web.dto.UserDto.RequestUserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	
	/* 회원가입 */
	@Override
	public Long userJoin(RequestUserDto dto) {
		/* 비밀번호 암호화 */
		dto.encryptPassword(encoder.encode(dto.getPassword()));
		
		User user = dto.toEntity();
		userRepository.save(user);
		log.info("DB에 회원 저장 성공");
		
		return user.getId();
	}

//	/* 아이디, 닉네임, 이메일 중복 유효성 검사 */
//	@Transactional(readOnly = true)
//	@Override
//	public boolean checkUsernameDuplication(String username) {
//		boolean usernameDuplicate = userRepository.existsByUsername(username);
//		return usernameDuplicate;
//	}
//
//	@Transactional(readOnly = true)
//	@Override
//	public boolean checkNicknameDuplication(String nickname) {
//		boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
//		return nicknameDuplicate;
//		
//	}
//
//	@Transactional(readOnly = true)
//	@Override
//	public boolean checkEmailDuplication(String email) {
//		boolean emailDuplicate = userRepository.existsByEmail(email);
//		return emailDuplicate;
//	}

	
}
