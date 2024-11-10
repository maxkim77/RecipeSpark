package com.project.RecipeSpark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/")
	public String root() {
		return "redirect:/main";
	}
	
	@GetMapping("/main")
	public String main() {
		return "main/index";
	}
	
	@GetMapping("/about")
	public String about() {
		return "main/about";
	}
	
}
