package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.RecipeForm;
import com.project.RecipeSpark.service.RecipeService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/recipe")
@RequiredArgsConstructor
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    // 레시피 작성 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createForm(RecipeForm recipeForm) {
        return "recipe/form";
    }

    // 레시피 작성 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createRecipe(@Valid RecipeForm recipeForm, 
                               BindingResult bindingResult, 
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/form";
        }

        User user = userService.getUser(principal.getName());
        recipeService.createRecipe(recipeForm.getTitle(), recipeForm.getContent(), user);
        return "redirect:/recipe/list";
    }

    // 레시피 수정 폼 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editForm/{recipeId}")
    public String editForm(@PathVariable Long recipeId, 
                           Principal principal, 
                           RedirectAttributes redirectAttributes) {
        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        
        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setTitle(recipe.getTitle());
        recipeForm.setContent(recipe.getContent());
        redirectAttributes.addFlashAttribute("recipeForm", recipeForm);
        redirectAttributes.addFlashAttribute("recipeId", recipeId);
        return "redirect:/recipe/editView";
    }

    // 수정 뷰 페이지
    @GetMapping("/editView")
    public String editView() {
        return "recipe/edit";
    }

    // 레시피 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{recipeId}")
    public String modifyRecipe(@PathVariable Long recipeId, 
                               @Valid RecipeForm recipeForm, 
                               BindingResult bindingResult, 
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/edit";
        }

        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        
        recipeService.modifyRecipe(recipeId, recipeForm.getTitle(), recipeForm.getContent());
        return "redirect:/recipe/detail/" + recipeId;
    }

    // 레시피 삭제 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{recipeId}")
    public String deleteRecipe(@PathVariable Long recipeId, Principal principal) {
        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
        
        recipeService.deleteRecipe(recipeId);
        return "redirect:/recipe/list";
    }
}
