package com.project.RecipeSpark.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.UserCreateForm;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    
    private final UserRepository userRepository;

    // 회원가입 페이지
    @GetMapping("/signup")
    public String showSignupForm(UserCreateForm userCreateForm) {
        return "auth/signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String handleSignup(@Valid UserCreateForm userCreateForm) {
        if (!userService.validatePassword(userCreateForm.getPassword1(), userCreateForm.getPassword2())) {
            // 비밀번호 불일치 시 오류 메시지를 전달하거나 다른 처리를 추가
            return "redirect:/users/signup?error=passwordMismatch";
        }
        userService.createUser(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        return "redirect:/users/login";
    }

    // 프로필 페이지
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails,HttpServletRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername())
        		.orElseThrow(()->new RuntimeException("user not found"));
        request.setAttribute("user",user);
    	return "auth/profile";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }
}
