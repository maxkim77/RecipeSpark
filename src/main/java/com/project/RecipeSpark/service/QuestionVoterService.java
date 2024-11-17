package com.project.RecipeSpark.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.QuestionVoter;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.QuestionVoterRepository;
import com.project.RecipeSpark.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionVoterService {

    private final QuestionVoterRepository questionVoterRepository;
    private final QuestionRepository questionRepository;

    /**
     * 특정 사용자가 특정 질문에 투표했는지 확인
     */
    @Transactional(readOnly = true)
    public boolean hasUserVoted(Question question, User user) {
        return questionVoterRepository.existsByQuestionAndUser(question, user);
    }

    /**
     * 투표 추가 처리
     */
    @Transactional
    public void addVote(Question question, User user) {
        // 사용자가 이미 투표했는지 확인
        if (!hasUserVoted(question, user)) {
            // 새로운 투표 객체 생성 및 저장
            QuestionVoter questionVoter = new QuestionVoter();
            questionVoter.setQuestion(question);
            questionVoter.setUser(user);
            questionVoterRepository.save(questionVoter);

            // 질문의 추천 수 증가
            questionVoter.setVoteCount(questionVoter.getVoteCount() + 1);
            questionRepository.save(question); // 변경된 상태 저장
        }
    }
    
    @Transactional
    public void removeVote(Question question, User user) {
    	if(hasUserVoted(question, user)) {
            QuestionVoter questionVoter = questionVoterRepository.findByQuestionAndUser(question, user);
            questionVoterRepository.delete(questionVoter);
            
            questionVoter.setVoteCount(questionVoter.getVoteCount() - 1);

    	}
    }
}
