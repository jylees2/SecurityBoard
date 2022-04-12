package com.jy.service.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jy.domain.Role;
import com.jy.domain.post.PostRepository;
import com.jy.domain.user.User;
import com.jy.service.post.PostService;
import com.jy.web.dto.PostDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostServiceTest {

	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Test
	public void 게시물_생성_조회() {
		
		log.info("현재 DB에 저장된 게시물 개수 : "+postRepository.count());
		// given
		User user = User.builder()
						.username("hellospring")
						.nickname("springdev")
						.email("spring@jy.com")
						.role(Role.USER)
						.build();
		
		PostDto.RequestDto post = PostDto.RequestDto.builder()
													.title("test title")
													.content("test content")
													.view(0)
													.user(user)
													.build();
		
		// when
		postService.save(post, user.getNickname());
		
		// then
		assertThat(postRepository.count()).isEqualTo(1);
	}
}
