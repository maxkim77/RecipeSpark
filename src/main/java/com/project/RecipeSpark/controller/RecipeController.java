package com.project.RecipeSpark.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.RecipeForm;
import com.project.RecipeSpark.service.RecipeService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    /**
     * 레시피 작성 폼 페이지
     */
    @GetMapping("/create")
    public String showCreateForm(RecipeForm recipeForm) {
        return "recipe/form";
    }

    /**
     * 레시피 작성 처리
     */
    @PostMapping("/create")
    public String createRecipe(@Valid RecipeForm recipeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/form";
        }

        User user = userService.getUser(principal.getName());
        MultipartFile imageFile = recipeForm.getImage();

        // 이미지 파일 유효성 검사
        if (imageFile == null || imageFile.isEmpty()) {
            bindingResult.rejectValue("image", "error.image", "Image file is required.");
            return "recipe/form";
        }

        recipeService.createRecipe(recipeForm.getTitle(), recipeForm.getContent(), user, imageFile);
        return "redirect:/recipe/list";
    }

    /**
     * 레시피 목록 페이지
     */
    @GetMapping("/list")
    public String showRecipeList(Model model) {
        List<Recipe> recipes = recipeService.getRecipe();
        model.addAttribute("recipes", recipes != null ? recipes : List.of());
        return "recipe/list";
    }

    /**
     * 레시피 상세 보기 페이지
     */
    @GetMapping("/detail/{reviewId}")
    public String showRecipeDetail(@PathVariable(name = "reviewId") Long reviewId, Model model) {
        Recipe recipe = recipeService.getRecipeById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        model.addAttribute("recipe", recipe);
        return "recipe/detail";
    }

    /**
     * 레시피 수정 폼 페이지
     */
    @GetMapping("/edit/{reviewId}")
    public String showEditForm(@PathVariable(name = "reviewId") Long reviewId, Principal principal, Model model) {
        Recipe recipe = recipeService.getRecipeById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this recipe");
        }

        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setTitle(recipe.getTitle());
        recipeForm.setContent(recipe.getContent());

        model.addAttribute("recipeForm", recipeForm);
        model.addAttribute("reviewId", reviewId);
        return "recipe/edit";
    }

    /**
     * 레시피 수정 처리
     */
    @PostMapping("/edit/{reviewId}")
    public String editRecipe(@PathVariable(name = "reviewId") Long reviewId, @Valid RecipeForm recipeForm, BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/edit";
        }

        Recipe recipe = recipeService.getRecipeById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this recipe");
        }

        MultipartFile imageFile = recipeForm.getImage();
        recipeService.modifyRecipe(reviewId, recipeForm.getTitle(), recipeForm.getContent(), imageFile);
        return "redirect:/recipe/list";
    }

    /**
     * 레시피 삭제 처리
     */
    @PostMapping("/delete/{reviewId}")
    public String deleteRecipe(@PathVariable(name = "reviewId") Long reviewId, Principal principal) {
        Recipe recipe = recipeService.getRecipeById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this recipe");
        }

        recipeService.deleteRecipe(reviewId);
        return "redirect:/recipe/list";
    }
}
