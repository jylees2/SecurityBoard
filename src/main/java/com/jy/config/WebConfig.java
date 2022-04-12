package com.jy.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jy.config.auth.LoginUserArgumentResolver;

import lombok.RequiredArgsConstructor;

/*
 * 스프링이 LoginUserArgumentResolver을 인식할 수 있도록 WebMvcConfigurer에 추가
 */
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final LoginUserArgumentResolver loginUserArgumentResolver;
	
	/* HandlerMethodArgumentResolver 는 항상 addArgumentResolver()를 통해 추가 */
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
	}

}
