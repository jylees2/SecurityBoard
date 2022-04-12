package com.jy;

import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.jy.domain.post.Post;
import com.jy.domain.post.PostRepository;

@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class SecurityBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityBoardApplication.class, args);
	}

//  @Bean
//  public CommandLineRunner runner(PostRepository postRepository) throws Exception {
//      return (args) -> {
//          IntStream.rangeClosed(1, 200).forEach(index ->
//              postRepository.save(Post.builder()
//                      .title("글" + index)
//                      .content("내용" + index)
//                      .writer("작성자"+index).build()));
//      };
//  }
}
