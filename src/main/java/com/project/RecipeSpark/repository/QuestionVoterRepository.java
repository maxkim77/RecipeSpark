package com.project.RecipeSpark.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.QuestionVoter;
import com.project.RecipeSpark.domain.User;

public interface QuestionVoterRepository extends JpaRepository<QuestionVoter, Long> {

    // User와 Question을 기준으로 QuestionVoter 조회
    @Query("SELECT qv FROM QuestionVoter qv WHERE qv.question = :question AND qv.user = :user")
    QuestionVoter findByQuestionAndUser(@Param("question") Question question, @Param("user") User user);

    // User와 Question을 기준으로 존재 여부 확인
    boolean existsByQuestionAndUser(Question question, User user);
}
