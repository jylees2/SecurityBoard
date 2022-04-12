package com.jy.service.comment;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jy.domain.comment.Comment;
import com.jy.domain.comment.CommentRepository;
import com.jy.domain.post.Post;
import com.jy.domain.post.PostRepository;
import com.jy.domain.user.User;
import com.jy.domain.user.UserRepository;
import com.jy.web.dto.CommentDto;
import com.jy.web.dto.CommentDto.RequestDto;
import com.jy.web.dto.CommentDto.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService{

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	
	/* CREATE */
	@Override
	public Long save(Long id, String nickname, RequestDto commentDto) {
		
		/* user, post 꺼내오기 */
		User user = userRepository.findByNickname(nickname); /* 댓글 작성자 정보*/
		Post post = postRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("댓글 생성 실패 : 해당 게시물이 존재하지 않습니다. "));
		
		/* 요청받은 commentDto에 post, user 추가 */
		commentDto.addPost(post);
		commentDto.addUser(user);
		
		/* DTO -> ENTITY */
		Comment comment = commentDto.toEntity();
		commentRepository.save(comment);
		
		log.info("댓글 저장 완료 : "+comment.getId());
		return comment.getId();
	}

	/* READ */
	@Override
	@Transactional(readOnly = true)
	public List<ResponseDto> findAll(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() ->
						new IllegalArgumentException("댓글 조회 실패 : 해당 게시물이 존재하지 않습니다."));
		
		/* 반환한 post 의 댓글을 List 컬렉션으로 반환 */
		List<Comment> comment = post.getComment();

		log.info("댓글 조회 완료");
		
		/**/
		return comment.stream().map(CommentDto.ResponseDto::new).collect(Collectors.toList());
	}

	/* UPDATE */
	@Override
	public void update(Long comment_id, CommentDto.RequestDto commentDto) {
		Comment comment = commentRepository.findById(comment_id).orElseThrow(() ->
								new IllegalArgumentException("댓글 수정 실패 : 해당 게시물이 존재하지 않습니다."));
		
		/* 요청 받은 수정 내용을 Comment 도메인 클래스의 update 메서드 이용해 수정*/
		comment.update(commentDto.getContent());
		log.info("댓글 수정 완료");
	}
	
	
	/* DELETE */
	@Override
	public void delete(Long comment_id) {
		Comment comment = commentRepository.findById(comment_id).orElseThrow(() ->
								new IllegalArgumentException("댓글 삭제 실패 : 해당 게시물이 존재하지 않습니다."));
		
		commentRepository.delete(comment);
		log.info("댓글 삭제 완료");
	}
	
}
