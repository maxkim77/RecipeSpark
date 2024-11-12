package com.project.RecipeSpark.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.RecipeSpark.domain.QuestionVoter;

public interface QuestionVoterRepository extends JpaRepository<QuestionVoter, Long> {
    Optional<QuestionVoter> findByQuestionQuestionIdAndUserUserId(Long long1, Long userId);
    int countByQuestionQuestionId(Long questionId);
}
