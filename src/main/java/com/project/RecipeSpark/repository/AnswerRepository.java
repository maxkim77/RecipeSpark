package com.project.RecipeSpark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.Question;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {

	List<Answer> findByQuestion(Question question);

    Optional<Answer> findByAnswerId(Long answerId);

}