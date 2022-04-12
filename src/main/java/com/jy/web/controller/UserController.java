package com.jy.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jy.service.user.UserService;
import com.jy.web.dto.UserDto;
import com.jy.web.validator.CheckEmailValidator;
import com.jy.web.validator.CheckNicknameValidator;
import com.jy.web.validator.CheckUsernameValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * 회원 관련 controller
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	
	/* 중복 체크 유효성 검사 */
	private final CheckUsernameValidator checkUsernameValidator;
	private final CheckNicknameValidator checkNicknameValidator;
	private final CheckEmailValidator checkEmailValidator;
	
	/* 커스텀 유효성 검증 */
	@InitBinder
	public void validatorBinder(WebDataBinder binder) {
		binder.addValidators(checkUsernameValidator);
		binder.addValidators(checkNicknameValidator);
		binder.addValidators(checkEmailValidator);
	}
	
	/* 회원 가입 폼으로 이동 */
	@GetMapping("/auth/join")
	public String join() {
		return "/user/user-join";
	}
	
	@PostMapping("/auth/joinProc")
	public String joinProc(@Valid UserDto.RequestUserDto userDto, BindingResult bindingResult, Model model) {
		
		/* 검증 */
		if(bindingResult.hasErrors()) {
			/* 회원가입 실패 시 입력 데이터 값 유지 */
			model.addAttribute("userDto", userDto);
			
			/* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
			Map<String, String> errorMap = new HashMap<>();
			
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
				log.info("회원가입 실패 ! error message : "+error.getDefaultMessage());
			}
			
			/* Model에 담아 view resolve */
			for(String key : errorMap.keySet()) {
				model.addAttribute(key, errorMap.get(key));
			}

			/* 회원가입 페이지로 리턴 */
			return "/user/user-join";
		}
		
		// 회원가입 성공 시
		userService.userJoin(userDto);
		log.info("회원가입 성공");
		return "redirect:/auth/login";
	}
	
//	/* 아이디, 닉네임, 이메일 중복 체크 */
//	@GetMapping("/auth/joinProc/{username}/exists")
//	public ResponseEntity<Boolean> checkUsernameDuplicate(@PathVariable String username){
//		return ResponseEntity.ok(userService.checkUsernameDuplication(username));
//	}
//	
//	@GetMapping("/auth/joinProc/{nickname}/exists")
//	public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname){
//		return ResponseEntity.ok(userService.checkUsernameDuplication(nickname));
//	}
//	
//	@GetMapping("/auth/joinProc/{email}/exists")
//	public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
//		return ResponseEntity.ok(userService.checkUsernameDuplication(email));
//	}
	
	/* 로그인 */
	@GetMapping("/auth/login")
	public String login(@RequestParam(value = "error", required = false) String error,
						@RequestParam(value = "exception", required = false) String exception,
						Model model) {
		
		/* 에러와 예외를 모델에 담아 view resolve */
		model.addAttribute("error", error);
		model.addAttribute("exception", exception);
		return "/user/user-login";
	}
	
	/* 로그아웃
	 * 기본적으로 스프링 시큐리티에서는 로그아웃이 POST 요청 방식이지만 GET 방식으로 우회
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		/* 인증 확인, 인증 객체를 꺼내옴 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		/* SecurityContextLogoutHanlder 통해 logout */
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		log.info("로그아웃 성공");
		return "redirect:/";
	}
}
