package com.project.RecipeSpark.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @GetMapping("/create/{questionId}")
    public String createForm(@PathVariable Integer questionId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("questionId", questionId);
        return "answer/form";
    }

    
    @PostMapping("/create/{questionId}")
    @PreAuthorize("isAuthenticated()")
    public String createAnswer(@PathVariable("questionId") Integer questionId,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("answerForm", answerForm); // 폼 데이터 유지
            redirectAttributes.addFlashAttribute("bindingResult", bindingResult); // 에러 메시지 유지
            return "redirect:/question/detail/" + questionId;
        }

        Question question = this.questionService.getQuestion(questionId);
        User user = this.userService.getUser(principal.getName());
        this.answerService.createAnswer(question, answerForm.getContent(), user);

        return "redirect:/question/detail/" + questionId;
    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editForm/{answerId}")
    public String editForm(@PathVariable("answerId") Integer answerId,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        Answer answer = answerService.getAnswerById(answerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

        checkAuthorization(answer, principal.getName());

        AnswerForm answerForm = new AnswerForm();
        answerForm.setContent(answer.getContent());
        redirectAttributes.addFlashAttribute("answerForm", answerForm);
        redirectAttributes.addFlashAttribute("answerId", answerId);

        return "redirect:/answer/editView";
    }

    @GetMapping("/editView")
    public String editView() {
        return "answer/edit";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{answerId}")
    public String modifyAnswer(@PathVariable("answerId") Integer answerId,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/answer/editForm/" + answerId;
        }

        Answer answer = answerService.getAnswerById(answerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

        checkAuthorization(answer, principal.getName());

        answerService.modifyAnswer(answer, answerForm.getContent());
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{answerId}")
    public String deleteAnswer(@PathVariable("answerId") Integer answerId
    							, Principal principal)
    {
    	
        Answer answer = answerService.getAnswerById(answerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found"));

        checkAuthorization(answer, principal.getName());

        answerService.deleteAnswer(answer);
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }

    // 권한 체크 메서드
    private void checkAuthorization(Answer answer, String username) {
        if (!answer.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정/삭제 권한이 없습니다.");
        }
    }
}
