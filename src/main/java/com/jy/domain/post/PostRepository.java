package com.jy.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>{
	/* 조회수 */
	@Modifying
	@Query("update Post p set p.view = p.view + 1 where p.id = :id")
	int updateView(@Param("id") Long id);
	
	// 키워드로 게시물 찾기
	Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
