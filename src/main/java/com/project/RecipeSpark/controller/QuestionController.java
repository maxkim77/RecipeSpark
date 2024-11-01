package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.QuestionForm;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    // 질문 작성 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(QuestionForm questionForm) {
        return "question/form";
    }

    // 질문 작성 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm, 
                                 BindingResult bindingResult, 
                                 Principal principal, 
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "question/form";
        }

        User user = userService.getUser(principal.getName());
        questionService.createQuestion(questionForm.getTitle(), questionForm.getContent(), user);
        return "redirect:/question/list";
    }

    // 질문 수정 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editForm/{questionId}")
    public String editForm(@PathVariable Integer questionId, 
                           Principal principal, 
                           RedirectAttributes redirectAttributes) {
        Question question = questionService.getQuestion(questionId);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        
        QuestionForm questionForm = new QuestionForm();
        questionForm.setTitle(question.getTitle());
        questionForm.setContent(question.getContent());
        redirectAttributes.addFlashAttribute("questionForm", questionForm);
        redirectAttributes.addFlashAttribute("questionId", questionId);
        return "redirect:/question/editView";
    }

    // 수정 뷰 페이지
    @GetMapping("/editView")
    public String editView() {
        return "question/edit";
    }

    // 질문 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{questionId}")
    public String modifyQuestion(@PathVariable Integer questionId, 
                                 @Valid QuestionForm questionForm, 
                                 BindingResult bindingResult, 
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question/edit";
        }

        Question question = questionService.getQuestion(questionId);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        
        questionService.modifyQuestion(question, questionForm.getTitle(), questionForm.getContent());
        return "redirect:/question/detail/" + questionId;
    }

    // 질문 삭제 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable Integer questionId, Principal principal) {
        Question question = questionService.getQuestion(questionId);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
        
        questionService.deleteQuestion(question);
        return "redirect:/question/list";
    }
}
