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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.AnswerForm;
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
    
    @GetMapping("/list")
    public String list(Model model
    				   ,@RequestParam(value="page",defaultValue="0") int page
    				   ,@RequestParam(value="kw", defaultValue="") String kw) {
    			Page<Question> paging = this.questionService.getList(page, kw);
    			model.addAttribute("paging",paging);
    			model.addAttribute("kw",kw);
				return "question_list";
    	
    }
    
    @GetMapping(value="/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
    	Question question = this.questionService.getQuestion(id);
    	model.addAttribute("question", question);
		return "question_detail";
    }
    		
    	

    // 질문 작성 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm) {
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
    
    // 질문 수정 처리  
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyQuestion(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		questionForm.setTitle(question.getTitle());
		questionForm.setContent(question.getContent());
    	return "question_form";
    }
    
    // 질문 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyQuestion(@Valid QuestionForm questionForm
    							, BindingResult bindingResult
    							, Principal principal
    							, @PathVariable("id") Integer id)
    {	
    	if(bindingResult.hasErrors()) {
    		return "qustion_form";
    	}
    	Question question = this.questionService.getQuestion(id);
    	if(!question.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없음 ");		
    	}
    	this.questionService.modifyQuestion(question, questionForm.getTitle(), question.getContent());
    	
    	return String.format("redirect:/question/detail/%s",id);
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
