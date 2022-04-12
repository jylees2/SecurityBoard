package com.jy.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jy.config.auth.LoginUser;
import com.jy.service.post.PostService;
import com.jy.web.dto.PostDto;
import com.jy.web.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PostApiController {
	
	private final PostService postService;
	
	/* CREATE */
	@PostMapping("/post")
	public ResponseEntity<?> save(@RequestBody PostDto.RequestDto post, @LoginUser UserDto.ResponseUserDto user) {
		log.info("post-api");
		postService.save(post, user.getNickname());
		return new ResponseEntity<>("{}", HttpStatus.CREATED);
	}
	
	/* READ */
	@GetMapping("/post/{id}")
	public ResponseEntity<?> read(@PathVariable Long id){
		postService.findById(id);
		return ResponseEntity.ok(id);
	}
	
	/* UPDATE */
	@PutMapping("/post/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PostDto.RequestDto post){
		postService.update(id, post);
		return ResponseEntity.ok(id);
	}
	
	/* DELETE */
	@DeleteMapping("/post/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		postService.delete(id);
		return ResponseEntity.ok(id);
	}
	

}
