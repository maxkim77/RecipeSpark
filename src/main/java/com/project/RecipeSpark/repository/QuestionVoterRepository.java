package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.QuestionVoter;

public interface QuestionVoterRepository extends JpaRepository<QuestionVoter,Long> {

	List<QuestionVoter> findByQuestionQuestionId(Long questionId);
	
	List<QuestionVoter> findByUserUserId(Long userId);
}
