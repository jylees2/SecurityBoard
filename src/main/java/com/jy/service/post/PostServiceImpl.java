package com.jy.service.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jy.domain.post.Post;
import com.jy.domain.post.PostRepository;
import com.jy.domain.user.User;
import com.jy.domain.user.UserRepository;
import com.jy.web.dto.PostDto;
import com.jy.web.dto.PostDto.RequestDto;
import com.jy.web.dto.PostDto.ResponseDto;
import com.jy.web.vo.PageVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class PostServiceImpl implements PostService{
	
	private static final int PAGE_POST_COUNT = 10; // 한 화면에 보일 컨텐츠 수를 static final 변수로 선언

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	/* create */
	@Override
	public Long save(PostDto.RequestDto dto, String nickname) {
		
		log.info("postService - save 메소드 작동");
		// user 정보를 가져와서 dto에 담아준다
		User user = userRepository.findByNickname(nickname); // nickname 으로 user 찾음
		dto.addUser(user);
		log.info("post dto - user 세팅 성공");
		
		// dto -> entity로 변환하여 db에 저장
		Post post = dto.toEntity();
		postRepository.save(post);
		log.info("postRepository save 완료");
		
		return post.getId();
	}

	/* read */
	@Override
	@Transactional(readOnly = true)
	public ResponseDto findById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		log.info("id로 찾기 완료");
		return new PostDto.ResponseDto(post); // new 생성자로 응답 메시지로 감싸서 반환
	}

	/* update - dirty checking
	 * user 객체 영속화, 영속화된 user 객체를 가져와 데이터 변경 -> 트랜잭션 끝날 때 자동으로 DB에 저장
	 */
	@Override
	public void update(Long id, RequestDto requestDto) {
		Post post = postRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		/* 수정 메서드 호출 */
		post.update(requestDto.getTitle(), requestDto.getContent());
		log.info("수정 완료");
	}

	/* delete */
	@Override
	public void delete(Long id) {
		Post post = postRepository.findById(id).orElseThrow(()->
				new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
		postRepository.delete(post);
		log.info("삭제 완료");		
	}

	@Override
	public int updateView(Long id) {
		return postRepository.updateView(id);
	}
	
	/* 리스트 페이징 */
	@Override
	@Transactional(readOnly = true)
	public Page<ResponseDto> getPageList(Pageable pageable, int pageNo) {
		pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id"));
		Page<Post> page = postRepository.findAll(pageable);
		
		// 데이터를 그대로 노출하는 것은 위험, DTO로 변환해서 리턴할 것
        Page<PostDto.ResponseDto> map = page.map(post -> new PostDto.ResponseDto(post.getId(), post.getTitle(), null, post.getWriter(), post.getView(), post.getCreatedDate(), post.getModifiedDate(), null, null));
        return map;
	}
	
	/* 검색 페이징 */
	@Override
	@Transactional(readOnly = true)
	public Page<ResponseDto> searchPageList(String keyword, Pageable pageable, int pageNo) {
		pageable = PageRequest.of(pageNo, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "id"));
		Page<Post> page = postRepository.findByTitleContaining(keyword, pageable);
		
		log.info("totalElements:"+page.getTotalElements());
        Page<PostDto.ResponseDto> map = page.map(post -> new PostDto.ResponseDto(post.getId(), post.getTitle(), null, post.getWriter(), post.getView(), post.getCreatedDate(), post.getModifiedDate(), null, null));

		return map;
	}
	
	/* 페이지 정보 반환 */
	@Override
	public PageVo getPageInfo(Page<ResponseDto> postList, int pageNo) {
		int totalPage = postList.getTotalPages();
		int startNumber = (int)((Math.floor(pageNo/PAGE_POST_COUNT)*PAGE_POST_COUNT)+1 <= totalPage ? (Math.floor(pageNo/PAGE_POST_COUNT)*PAGE_POST_COUNT)+1 : totalPage);
		// 현재 페이지를 통해 현재 페이지 그룹의 시작 페이지를 구함
		
		int endNumber = (startNumber + PAGE_POST_COUNT-1 < totalPage ? startNumber + PAGE_POST_COUNT-1 : totalPage);
		// 전체 페이지 수와 현재 페이지 그룹의 시작 페이지를 통해 현재 페이지 그룹의 마지막 페이지를 구함
		
		boolean hasPrev = postList.hasPrevious();
		boolean hasNext = postList.hasNext();
		
		/* 화면에는 원래 페이지 인덱스+1 로 출력됨을 주의 */		
		int prevIndex = postList.previousOrFirstPageable().getPageNumber()+1;
		int nextIndex = postList.nextOrLastPageable().getPageNumber()+1;
		
		return new PageVo(totalPage, startNumber, endNumber, hasPrev, hasNext, prevIndex, nextIndex);
	}

	
}
