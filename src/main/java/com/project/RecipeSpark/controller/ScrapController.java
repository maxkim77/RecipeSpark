package com.project.RecipeSpark.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.Scrap;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.service.RecipeService;
import com.project.RecipeSpark.service.ScrapService;
import com.project.RecipeSpark.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final UserService userService;
    private final RecipeService recipeService;

    /**
     * 레시피 스크랩 추가
     */
    @PostMapping("/recipe/scrap/{recipeId}")
    public String scrapRecipe(@PathVariable("recipeId") Long recipeId, Principal principal) {
        User user = userService.getUser(principal.getName());
        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        scrapService.addScrap(user, recipe);
        return "redirect:/recipe/detail/" + recipeId;
    }

    /**
     * 레시피 스크랩 해제
     */
    @PostMapping("/recipe/unscrap/{recipeId}")
    public String unscrapRecipe(@PathVariable("recipeId") Long recipeId, Principal principal) {
        User user = userService.getUser(principal.getName());
        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        scrapService.removeScrap(user, recipe);
        return "redirect:/recipe/detail/" + recipeId;
    }

    /**
     * 스크랩한 레시피 목록 보기
     * 
     * @param principal 현재 로그인된 사용자 정보
     * @param model UI에 전달할 데이터
     * @return 스크랩 목록 페이지
     */
    @GetMapping("/scrap/list")
    public String showScrapList(Principal principal, Model model) {
        // 로그인한 사용자 정보 가져오기
        User user = userService.getUser(principal.getName());

        // 사용자가 스크랩한 레시피 목록 가져오기
        List<Scrap> scraps = scrapService.getScrapsByUser(user);

        // 모델에 데이터 추가
        model.addAttribute("scraps", scraps);

        // 스크랩 목록 페이지 반환
        return "scrap/list"; // 스크랩 목록을 보여줄 HTML 템플릿 경로
    }
}
