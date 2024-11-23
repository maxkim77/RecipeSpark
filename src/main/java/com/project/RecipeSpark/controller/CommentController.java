package com.project.RecipeSpark.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.project.RecipeSpark.domain.Comment;
import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.form.CommentForm;
import com.project.RecipeSpark.service.CommentService;
import com.project.RecipeSpark.service.RecipeService;
import com.project.RecipeSpark.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final RecipeService recipeService;
    private final UserService userService;

    @PostMapping("/recipe/{recipeId}/comment")
    public String addComment(@PathVariable("recipeId") Long recipeId,
                             @RequestParam("content") String content,
                             Principal principal) {
        User user = userService.getUser(principal.getName());
        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        commentService.addComment(user, recipe, content);

        return "redirect:/recipe/detail/" + recipeId;
    }

    @GetMapping("/recipe/{recipeId}/comment/{commentId}/edit")
    public String showEditForm(@PathVariable("recipeId") Long recipeId,
                               @PathVariable("commentId") Long commentId,
                               Principal principal,
                               Model model) {
        User user = userService.getUser(principal.getName());
        Comment comment = commentService.getCommentById(commentId);

        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You can only edit your own comments.");
        }

        CommentForm commentForm = new CommentForm();
        commentForm.setContent(comment.getContent());

        model.addAttribute("commentForm", commentForm);
        model.addAttribute("recipeId", recipeId);
        model.addAttribute("commentId", commentId);

        return "comment/form";
    }

    @PostMapping("/recipe/{recipeId}/comment/{commentId}/edit")
    public String editComment(@PathVariable("recipeId") Long recipeId,
                              @PathVariable("commentId") Long commentId,
                              @Valid CommentForm commentForm,
                              BindingResult bindingResult,
                              Principal principal) {
        if (bindingResult.hasErrors()) {
            return "comment/form";
        }

        User user = userService.getUser(principal.getName());
        commentService.updateComment(commentId, user, commentForm.getContent());

        return "redirect:/recipe/detail/" + recipeId;
    }

    @PostMapping("/recipe/{recipeId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("recipeId") Long recipeId,
                                @PathVariable("commentId") Long commentId,
                                Principal principal) {
        User user = userService.getUser(principal.getName());
        commentService.deleteComment(commentId, user);

        return "redirect:/recipe/detail/" + recipeId;
    }
}
