package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTitleContaining(String title);
}
