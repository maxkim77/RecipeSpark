package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.AnswerVoter;

public interface AnswerVoterRepository extends JpaRepository<AnswerVoter, Long>{
	List<AnswerVoter> findByAnswerAnswerId(Long answerId);
	List<AnswerVoter> findByUserUserId(Long userId);
}
