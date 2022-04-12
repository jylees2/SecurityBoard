package com.jy.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jy.domain.user.User;
import com.jy.domain.user.UserRepository;
import com.jy.web.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	
	@Autowired
	private UserRepository userRepository;
	
	/* BCryptPasswordEncoder 생성자로 빈 등록 */
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@WithAnonymousUser
	public void 비로그인_사용자_접근() throws Exception{
		
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	@WithAnonymousUser
	public void 비로그인_사용자_접근_실패() throws Exception{

		mockMvc.perform(post("/post/write"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
	
	@Test
	@Transactional
	public void 로그인_성공() throws Exception{
		
		String rawPassword = "password!33";
		String encoderedPassword = passwordEncoder.encode(rawPassword);

		UserDto.RequestUserDto loginDto =
								UserDto.RequestUserDto.builder()
													.username("user")
													.password(encoderedPassword)
													.nickname("springdev")
													.email("min@nate.com")
													.build();
		
		User loginInfo = loginDto.toEntity();
		userRepository.save(loginInfo);
		
		/* 어떻게 코드를 짜야 할까..? */
	}
}
