package com.project.RecipeSpark.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.RecipeSpark.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTitleContaining(String title);

    Page<Recipe> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, org.springframework.data.domain.Pageable pageable);
}
