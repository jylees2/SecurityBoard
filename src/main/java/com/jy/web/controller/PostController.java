package com.jy.web.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.jy.config.auth.LoginUser;
import com.jy.service.post.PostService;
import com.jy.web.dto.CommentDto;
import com.jy.web.dto.PostDto;
import com.jy.web.dto.UserDto;
import com.jy.web.vo.PageVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostController {
	
	private final PostService postService;
	
	/* 리스트 화면 & 페이징 구현 */
	@GetMapping("/")
	public String home(Model model, 
			@RequestParam(required = false, defaultValue = "0", value="page") int pageNo,
			Pageable pageable,
			@LoginUser UserDto.ResponseUserDto user) {
		
		// 로그인한 유저라면
		if(user != null) {
			model.addAttribute("user", user);
		}
		
		// 클라이언트 페이지에서 받은 pageNo와 실제 접근 페이지는 다름. Page는 페이지가 0부터 시작
		// 따라서 실제 접근 페이지는 pageNo-1 처리 -> 컨트롤러에서 안 하고 싶은데..
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);
	
		Page<PostDto.ResponseDto> postList = postService.getPageList(pageable, pageNo); // page 객체 생성
		PageVo pageVo = postService.getPageInfo(postList, pageNo); // page 객체 관련 정보 가져옴
		
		/* 페이지 인덱스 모음 */
		ArrayList<Integer> pageIndex = new ArrayList<>();
		for(int i = pageVo.startNumber; i <= pageVo.endNumber; i++) {
			pageIndex.add(i);
		}
		model.addAttribute("postList", postList);
		
		/* view 단으로 넘길 페이지 관련 정보 */
		model.addAttribute("pageNo", pageNo); // Model에 넘기는 pageNo은 실제 접근 페이지 번호, 따라서 view단에서는 pageNo+1 처리
		model.addAttribute("pageIndex", pageIndex); // 페이지 번호 리스트
		
//		model.addAttribute("hasNext", pageVo.hasNext); // 다음 페이지 유무
//		model.addAttribute("hasPrev", pageVo.hasPrev); // 이전 페이지 유무
//		model.addAttribute("totalPage", pageVo.totalPage); // 마지막 페이지 번호
//		model.addAttribute("next", pageable.next().getPageNumber()); // 다음 페이지 번호
//		model.addAttribute("prev", pageable.previousOrFirst().getPageNumber()); // 이전 페이지 번호
		
		model.addAttribute("pageVo", pageVo); // page 정보를 담은 객체
		return "index";
	}
	
	/* 게시물 등록 폼 */
	@GetMapping("/post/write")
	public String write(@LoginUser UserDto.ResponseUserDto user, Model model) {
		
		if(user != null) {
			model.addAttribute("user", user);
		}
		return "post/post-write";
	}
	
	/* 게시물 상세 보기 */
	@GetMapping("/post/read/{id}")
	public String read(@PathVariable Long id, Model model, @LoginUser UserDto.ResponseUserDto user){
		
		/* 읽는 즉시 조회수 +1 */
		postService.updateView(id);
		
		/* post 와 comment 가져오기 */
		PostDto.ResponseDto post = postService.findById(id);
		List<CommentDto.ResponseDto> comment = post.getComment();
		log.info("댓글 개수 : "+comment.size());
		/* 게시물 */
		model.addAttribute("post", post);
		
		/* 댓글 유무 확인 */
		if(comment != null && !comment.isEmpty()) {
			model.addAttribute("comment", comment);
			
			log.info("comment 크기 : "+comment.size());
		}
		
		/* 사용자 확인
		 * 게시물 작성자 본인 확인
		 * 댓글 작성자 본인 확인
		 */
		if(user != null) {
			
			model.addAttribute("user", user);
			
			/* 게시물 작성자 본인 확인 */
			if( post.getUserId().equals(user.getId()) ) {
				model.addAttribute("postWriter", true);
			}
			
			/* 댓글 작성자 본인 확인 */
			if( comment.stream().anyMatch( s -> s.getUserId().equals(user.getId())) ) {
				model.addAttribute("commentWriter", true);
			}
			/*
			 for(int i=0; i<comment.size(); i++){
			 	boolean commentWriter = comment.get(i).getUserId().equals(user.getId());
			 	model.addAttribute("commentWriter", commentWriter);
			 }
			 */
		}
		
		return "post/post-read";
	}
	
	/* 글 수정 */
	@GetMapping("/post/update/{id}")
	public String update(@PathVariable Long id, Model model, @LoginUser UserDto.ResponseUserDto user) {
		
		PostDto.ResponseDto post = postService.findById(id);
		
		if(user != null) {
			model.addAttribute("user", user);
		}
		
		model.addAttribute("post", post);
		return "post/post-update";
	}
	
	/* 글 검색 */
	@GetMapping("/post/search")
	public String search(@RequestParam String keyword, Model model,
				@RequestParam(required = false, defaultValue = "0", value="page") int pageNo,
				Pageable pageable, @LoginUser UserDto.ResponseUserDto user) {
		pageNo = (pageNo == 0) ? 0 : (pageNo - 1);

		Page<PostDto.ResponseDto> searchList = postService.searchPageList(keyword, pageable, pageNo);
		PageVo pageVo = postService.getPageInfo(searchList, pageNo);
		
		/* 페이지 인덱스 모음 */
		ArrayList<Integer> pageIndex = new ArrayList<>();
		for(int i = pageVo.startNumber; i <= pageVo.endNumber; i++) {
			pageIndex.add(i);
		}
		
		model.addAttribute("searchList", searchList);
		model.addAttribute("keyword", keyword);
		
		/* view 단으로 넘기는 페이지 관련 정보 */
		model.addAttribute("pageIndex", pageIndex); // 페이지 번호 리스트
		model.addAttribute("pageVo", pageVo);
		
		if(user != null) {
			model.addAttribute("user", user);
		}
		
		return "post/post-search";
	}

}
