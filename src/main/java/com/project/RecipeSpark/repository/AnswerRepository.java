package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByQuestion(Question question);

    List<Answer> findByQuestion_QuestionId(Long questionId);
}
