package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;

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

    @GetMapping("/create")
    public String showCreateForm(RecipeForm recipeForm) {
        return "recipe/form";
    }

    @PostMapping("/create")
    public String createRecipe(@Valid RecipeForm recipeForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/form";
        }

        User user = userService.getUser(principal.getName());
        recipeService.createRecipe(recipeForm.getTitle(), recipeForm.getContent(), user, recipeForm.getImage());
        return "redirect:/recipe/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this recipe");
        }

        RecipeForm recipeForm = new RecipeForm();
        recipeForm.setTitle(recipe.getTitle());
        recipeForm.setContent(recipe.getContent());

        redirectAttributes.addFlashAttribute("recipeForm", recipeForm);
        return "recipe/edit";
    }

    @PostMapping("/edit/{id}")
    public String editRecipe(@PathVariable Long id, @Valid RecipeForm recipeForm, BindingResult bindingResult,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            return "recipe/edit";
        }

        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit this recipe");
        }

        recipeService.modifyRecipe(id, recipeForm.getTitle(), recipeForm.getContent(), recipeForm.getImage());
        return "redirect:/recipe/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id, Principal principal) {
        Recipe recipe = recipeService.getRecipeById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (!recipe.getUser().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this recipe");
        }

        recipeService.deleteRecipe(id);
        return "redirect:/recipe/list";
    }
}
