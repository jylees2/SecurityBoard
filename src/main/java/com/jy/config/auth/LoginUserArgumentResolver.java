package com.jy.config.auth;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jy.web.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver{
	
	private final HttpSession httpSession;
	
	/* @LoginUser 어노테이션이 붙었고, 파라미터 클래스 타입이 UserDto.ResponseDto 타입인지 판단 후 true */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		/* @LoginUser 어노테이션이 들어있는가? */
		boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
		
		/* SessionUser 클래스 타입의 파라미터에 @LoginUser 어노테이션이 사용되었는가? */
		boolean isUserClass = UserDto.ResponseUserDto.class.equals(parameter.getParameterType());
		
		return isLoginUserAnnotation && isUserClass;
	}

	/* 파라미터에 전달할 객체 생성 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		return httpSession.getAttribute("user");
	}
	

}
