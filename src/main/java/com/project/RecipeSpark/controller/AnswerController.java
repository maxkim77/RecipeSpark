package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.project.RecipeSpark.domain.Answer;
import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.AnswerForm;
import com.project.RecipeSpark.service.AnswerService;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createAnswer(@RequestParam Integer questionId,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/question/detail/" + questionId;
        }

        Question question = questionService.getQuestion(questionId);
        User user = userService.getUser(principal.getName());
        answerService.createAnswer(question, answerForm.getContent(), user);

        return "redirect:/question/detail/" + questionId;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyAnswer(@RequestParam Integer answerId,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/answer/editForm/" + answerId;
        }

        Answer answer = answerService.getAnswerById(answerId.longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));
        checkAuthorization(answer, principal.getName());

        answerService.updateAnswerContent(answer, answerForm.getContent());
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete")
    public String deleteAnswer(@RequestParam Integer answerId, Principal principal) {
        Answer answer = answerService.getAnswerById(answerId.longValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));
        checkAuthorization(answer, principal.getName());

        answerService.deleteAnswer(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }

    private void checkAuthorization(Answer answer, String username) {
        if (!answer.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }
}
