package com.project.RecipeSpark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.AnswerVoter;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AnswerVoterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerVoterService {

    private final AnswerVoterRepository answerVoterRepository;

    @Transactional(readOnly = true)
    public boolean hasUserVoted(Answer answer, User user) {
        return answerVoterRepository
                .findByAnswer_AnswerIdAndUser_UserId(answer.getAnswerId(), user.getUserId())
                .isPresent();
    }

    @Transactional
    public void addVote(Answer answer, User user) {
        if (!hasUserVoted(answer, user)) {
            AnswerVoter answerVoter = new AnswerVoter();
            answerVoter.setAnswer(answer);
            answerVoter.setUser(user);
            answerVoterRepository.save(answerVoter);

            // 투표 수 증가
            answer.setVoteCount(answer.getVoteCount() + 1);
        }
    }

    @Transactional
    public void removeVote(Answer answer, User user) {
        answerVoterRepository.findByAnswer_AnswerIdAndUser_UserId(answer.getAnswerId(), user.getUserId())
                .ifPresent(answerVoter -> {
                    answerVoterRepository.delete(answerVoter);

                    // 투표 수 감소
                    answer.setVoteCount(answer.getVoteCount() - 1);
                });
    }

    @Transactional
    public void toggleVote(Answer answer, User user) {
        if (hasUserVoted(answer, user)) {
            removeVote(answer, user);
        } else {
            addVote(answer, user);
        }
    }
}
