package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.RecipeSpark.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	Question findByTitle(String title);
	
	Question findByTitleAndContent(String title, String content);
	
	List<Question> findByTitleLike(String title);
	
	Page<Question> findAll(Pageable pageable);
	
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("SELECT q FROM Question q"
			+ "LEFT OUTER JOIN USER u ON Q.authorId=u.userId"
			+ "LEFT OUTER JOIN Answer a ON a.question=q"
			+ "LEFT OUTER JOIN User au ON a.author = au.userId"
			+ "WHERE q.title LIKE %:kw%"
			+ "OR q.content LIKE %:kw% "
			+ "OR u.username LIKE %:KW% "
			+ "OR au.username LIKE %:KW% "
			)
	List<Question> findAllByKeyword(@Param("kw") String keyword, Pageable pageable);
}
