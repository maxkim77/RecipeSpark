package com.project.RecipeSpark.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerVoterService answerVoterService;

    @Transactional
    public Answer createAnswer(Question question, String content, User author) {
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setContent(content);
        answer.setAuthor(author);
        answer.setVoteCount(0);
        answer.setCreateDate(LocalDateTime.now());
        return answerRepository.save(answer);
    }

    public Optional<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id.intValue());
    }

    @Transactional
    public void updateAnswerContent(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Answer answer) {
        answerRepository.delete(answer);
    }

    public List<Answer> getAnswersByQuestion(Question question) {
        return answerRepository.findByQuestion(question);
    }

    public List<Answer> getAnswersByQuestionIdWithVoteStatus(Long questionId, User user) {
        List<Answer> answers = answerRepository.findByQuestion_QuestionId(questionId);

        // 투표 여부를 설정
        answers.forEach(answer -> {
            boolean hasVoted = answerVoterService.hasUserVoted(answer, user);
            answer.setHasVoted(hasVoted); // 필요 시 설정
        });

        return answers;
    }


    public void toggleVote(Answer answer, User user) {
        if (answerVoterService.hasUserVoted(answer, user)) {
            answerVoterService.removeVote(answer, user);
        } else {
            answerVoterService.addVote(answer, user);
        }
    }
}
