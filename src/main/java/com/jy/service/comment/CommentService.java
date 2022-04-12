package com.jy.service.comment;

import java.util.List;

import com.jy.web.dto.CommentDto;

public interface CommentService {

	/* CREATE */
	Long save(Long id, String nickname, CommentDto.RequestDto commentDto);
	
	/* READ */
	List<CommentDto.ResponseDto> findAll(Long id);
	
	/* UPDATE */
	void update(Long comment_id, CommentDto.RequestDto commentDto);
	
	/* DELETE */
	void delete(Long comment_id);
}
