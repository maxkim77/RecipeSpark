package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.QuestionVoterService;
import com.project.RecipeSpark.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionVoterController {

    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionVoterService questionVoterService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/vote")
    public String vote(Principal principal, @RequestParam("questionId") Integer questionId) {
        Question question = questionService.getQuestion(questionId);
        User user = userService.getUser(principal.getName());

        // 사용자가 이미 투표했는지 확인하고 처리
        if (questionVoterService.hasUserVoted(question, user)) {
            questionVoterService.removeVote(question, user);
        } else {
            questionVoterService.addVote(question, user);
        }

        return String.format("redirect:/question/detail/%d", questionId);
    }
}
