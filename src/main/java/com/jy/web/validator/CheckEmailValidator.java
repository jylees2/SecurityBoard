package com.jy.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.jy.domain.user.UserRepository;
import com.jy.web.dto.UserDto;
import com.jy.web.dto.UserDto.RequestUserDto;

import lombok.RequiredArgsConstructor;

/* 이메일 중복 확인 유효성 검증을 위한 커스텀 Validator 클래스 */

@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<UserDto.RequestUserDto>{
	private final UserRepository userRepository;

	@Override
	protected void doValidate(RequestUserDto dto, Errors errors) {
		if(userRepository.existsByEmail(dto.toEntity().getEmail())) {
			/* 중복인 경우 */
			errors.rejectValue("email","이메일 중복 오류", "이미 사용 중인 이메일입니다.");
		}
	}
}
