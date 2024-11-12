package com.project.RecipeSpark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.QuestionVoter;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.QuestionVoterRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionVoterService {
    private final QuestionVoterRepository questionVoterRepository;

    @Transactional
    public void vote(Question question, User user) {
        questionVoterRepository.findByQuestionQuestionIdAndUserUserId(question.getQuestionId(), user.getUserId())
            .ifPresentOrElse(
                questionVoterRepository::delete,
                () -> {
                    QuestionVoter questionVoter = new QuestionVoter();
                    questionVoter.setQuestion(question);
                    questionVoter.setUser(user);
                    questionVoterRepository.save(questionVoter);
                }
            );
    }
}
