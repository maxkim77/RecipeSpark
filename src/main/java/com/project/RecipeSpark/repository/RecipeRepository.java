package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	
	List<Recipe> findByTitleContaining(String title);
	
	List<Recipe> findByUserId(Long userId);
	
	Page<Recipe> findAll(Pageable pageable);

}
