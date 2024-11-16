package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.AnswerForm;
import com.project.RecipeSpark.form.QuestionForm;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.QuestionVoterService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionVoterService questionVoterService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Principal principal) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("answerForm", new AnswerForm());

        // 현재 사용자의 투표 여부 확인
        if (principal != null) {
            User user = userService.getUser(principal.getName());
            boolean hasVoted = questionVoterService.hasUserVoted(question, user);
            model.addAttribute("hasVoted", hasVoted);
        } else {
            model.addAttribute("hasVoted", false); // 비로그인 사용자는 false로 설정
        }

        return "question/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm) {
        return "question/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question/form";
        }
        User user = userService.getUser(principal.getName());
        questionService.createQuestion(questionForm.getTitle(), questionForm.getContent(), user);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyQuestion(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = validateUserPermission(id, principal);
        questionForm.setTitle(question.getTitle());
        questionForm.setContent(question.getContent());
        return "question/form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyQuestion(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,
                                 Principal principal,
                                 @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question/form";
        }
        Question question = validateUserPermission(id, principal);
        questionService.modifyQuestion(question, questionForm.getTitle(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{questionId}")
    public String deleteQuestion(@PathVariable("questionId") Integer questionId, Principal principal) {
        Question question = validateUserPermission(questionId, principal);
        questionService.deleteQuestion(question);
        return "redirect:/question/list";
    }

    // 권한 검증 메서드 (수정하지 않고 유지)
    private Question validateUserPermission(Integer questionId, Principal principal) {
        Question question = questionService.getQuestion(questionId);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        return question;
    }
}
