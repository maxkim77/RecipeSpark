package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.AnswerService;
import com.project.RecipeSpark.service.AnswerVoterService;
import com.project.RecipeSpark.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/vote/answer")
@RequiredArgsConstructor
public class AnswerVoterController {

    private final AnswerService answerService;
    private final AnswerVoterService answerVoterService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/toggle")
    public String toggleVote(@RequestParam Long answerId, Principal principal) {
        // Answer와 User를 가져옵니다.
        Answer answer = answerService.getAnswerById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid answer ID: " + answerId));
        User user = userService.getUser(principal.getName());

        // 투표 상태를 토글합니다.
        answerVoterService.toggleVote(answer, user);

        // 연결된 질문 페이지로 리다이렉트
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }
}
