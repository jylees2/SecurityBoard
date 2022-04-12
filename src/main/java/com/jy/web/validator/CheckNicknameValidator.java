package com.jy.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.jy.domain.user.UserRepository;
import com.jy.web.dto.UserDto;
import com.jy.web.dto.UserDto.RequestUserDto;

import lombok.RequiredArgsConstructor;

/* 닉네임 중복 확인 유효성 검증을 위한 커스텀 Validator 클래스 */

@RequiredArgsConstructor
@Component
public class CheckNicknameValidator extends AbstractValidator<UserDto.RequestUserDto> {
	private final UserRepository userRepository;

	@Override
	protected void doValidate(RequestUserDto dto, Errors errors) {
		if(userRepository.existsByNickname(dto.toEntity().getNickname())) {
			/* 중복인 경우 */
			errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용 중인 닉네임입니다.");
		}
	}
}
