package com.project.RecipeSpark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.AnswerVoter;

public interface AnswerVoterRepository extends JpaRepository<AnswerVoter, Long> {
    Optional<AnswerVoter> findByAnswer_AnswerIdAndUser_UserId(Long answerId, Long userId);
}
