package com.project.RecipeSpark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.QuestionVoter;
import com.project.RecipeSpark.domain.User;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
	
	Question findByTitle(String title);
	
	Question findByTitleAndContent(String title, String content);
	
	List<Question> findByTitleLike(String title);
	
	Page<Question> findAll(Pageable pageable);
	
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("SELECT q FROM Question q " +
		       "LEFT JOIN q.author u " +
		       "LEFT JOIN q.answerList a " + // 필드명을 answerList로 수정
		       "LEFT JOIN a.author au " +
		       "WHERE q.title LIKE %:kw% OR q.content LIKE %:kw% " +
		       "OR u.username LIKE %:kw% OR au.username LIKE %:kw%")
		Page<Question> findAllByKeyword(@Param("kw") String keyword, Pageable pageable);


    Optional<Question> findByQuestionId(Long questionId);
    @Query("SELECT qv FROM QuestionVoter qv WHERE qv.question = :question AND qv.user = :user")
    Optional<QuestionVoter> findByQuestionAndUser(@Param("question") Question question, @Param("user") User user);


}
