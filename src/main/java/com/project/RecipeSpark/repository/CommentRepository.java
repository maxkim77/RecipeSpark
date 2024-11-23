package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Comment;
import com.project.RecipeSpark.domain.Recipe;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeOrderByCreatedAtDesc(Recipe recipe);
}
