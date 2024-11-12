package com.project.RecipeSpark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.AnswerService;
import com.project.RecipeSpark.service.AnswerVoterService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AnswerVoterController {

    private final AnswerVoterService answerVoterService;
    private final AnswerService answerService;

    @PostMapping("/answer/vote")
    public String vote(@RequestParam Long answerId, @AuthenticationPrincipal User user) {
        // Answer를 찾고, 존재할 경우 투표 처리
        Answer answer = answerService.findByAnswerId(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid answer ID: " + answerId));
        answerVoterService.vote(answer.getAnswerId(), user.getUserId());

        // Answer와 연결된 질문 ID 찾기
        Long questionId = answer.getQuestion().getQuestionId();
        return "redirect:/question/detail/" + questionId;
    }
}
