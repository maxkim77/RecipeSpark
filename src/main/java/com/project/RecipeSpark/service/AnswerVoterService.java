package com.project.RecipeSpark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.AnswerVoter;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AnswerVoterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerVoterService {
    private final AnswerVoterRepository answerVoterRepository;

    @Transactional
    public void vote(Long answerId, Long userId) {
        answerVoterRepository.findByAnswer_AnswerIdAndUser_UserId(answerId, userId)
            .ifPresentOrElse(
                existingVote -> {
                    // 이미 투표가 존재하는 경우 처리 로직
                    System.out.println("이미 투표를 했습니다.");
                },
                () -> {
                    // 새로운 투표 생성
                    AnswerVoter answerVoter = new AnswerVoter();

                    // 관계 객체 생성
                    Answer answer = new Answer();
                    answer.setAnswerId(answerId);

                    User user = new User();
                    user.setUserId(userId);

                    answerVoter.setAnswer(answer);
                    answerVoter.setUser(user);

                    // 저장
                    answerVoterRepository.save(answerVoter);
                    System.out.println("새로운 투표를 등록했습니다.");
                }
            );
    }
}

