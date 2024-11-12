package com.project.RecipeSpark.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.QuestionVoterService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QuestionVoterController {
    private final QuestionVoterService questionVoterService;
    private final QuestionService questionService;

    @PostMapping("/question/vote")
    public String vote(@RequestParam Long questionId, @AuthenticationPrincipal User user) {
        questionService.findById(questionId).ifPresent(question -> questionVoterService.vote(question, user));
        return "redirect:/question/detail/" + questionId;
    }
}
