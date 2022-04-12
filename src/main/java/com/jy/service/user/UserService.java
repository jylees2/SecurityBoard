package com.jy.service.user;

import com.jy.web.dto.UserDto;

public interface UserService {

	/* 회원가입 */
	Long userJoin(UserDto.RequestUserDto dto);
	
//	/* 아이디, 닉네임, 이메일 중복 여부 확인 */
//	boolean checkUsernameDuplication(String username);
//	boolean checkNicknameDuplication(String nickname);
//	boolean checkEmailDuplication(String email);
	
}
