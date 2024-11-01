package com.project.RecipeSpark.controller;

import java.security.Principal;

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

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{questionId}")
    public String createForm(@PathVariable Integer questionId, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("questionId",questionId);
    	return "answer/form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{questionId}")
    public String createAnswer(@PathVariable Integer questionId, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal,  RedirectAttributes redirectAttributes) {
    	if(bindingResult.hasErrors()) {
    		redirectAttributes.addFlashAttribute("answerForm",answerForm);
    		return "redirect:/answer/create/"+questionId;
    	}
    	Question question = questionService.getQuestion(questionId);
   	    if (question == null) {
    	  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
    	 User user = userService.getUser(principal.getName());
    	 answerService.createAnswer(question, answerForm.getContent(), user);
    	 return "redirect:/question/detail/" + questionId;
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editFrom/{answerId}")
    public String editForm(@PathVariable Integer answerId,Principal principal, RedirectAttributes redirectAttributes) {
    	Answer answer = answerService.getAnswer(answerId);
    	if(!answer.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
    	}
    	AnswerForm answerForm = new AnswerForm();
    	answerForm.setContent(answer.getContent());
    	redirectAttributes.addFlashAttribute("answerForm",answerForm);
        redirectAttributes.addFlashAttribute("answerId", answerId);

		return "redirect:/answer/editView";
    }
    @GetMapping("/editView")
    public String editView() {
    	return "answer/edit";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{answerId}")
    public String modifyAnswer(@PathVariable Integer answerId,
    						  @Valid AnswerForm answerForm,
    						  BindingResult bindingResult,
    						  Principal principal) {
    	if(bindingResult.hasErrors()) {
    		return "redirect:/answer/editForm/"+answerId;
    	}
    	Answer answer = answerService.getAnswer(answerId);
    	if(!answer.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
    	}
    	answerService.modifyAnswer(answer, answerForm.getContent());
        return "redirect:/question/detail/" + answer.getQuestion().getQuestionId();
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{answerId}")
    public String deleteAnswer(@PathVariable Integer answerId, Principal principal) {
    	Answer answer = answerService.getAnswer(answerId);
    	if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");

    	}
    	answerService.deleteAnswer(answer);
    	return "redirect:/question/detail/"+answer.getQuestion().getQuestionId();
    }
  
}
