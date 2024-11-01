package com.project.RecipeSpark.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.RecipeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {
	private final RecipeRepository recipeRepository;
	
	public Recipe createRecipe(String title, String content, User user) {
		return null;
	}

	public Optional<Recipe> getRecipeById(Long id){
		return recipeRepository.findById(id);
	}
	
	public Page<Recipe> findAll(Pageable pageable){
		return recipeRepository.findAll(pageable);
	}
	
	public List<Recipe> findByTitle(String title) {
		return recipeRepository.findByTitleContaining(title);
	}
	
	@Transactional
	public Recipe modifyRecipe(Long id, String newTitle, String newContent) {
		Recipe recipe = recipeRepository.findById(id).orElseThrow(
				()-> new RuntimeException("Recipe not found with id:"+id));
		recipe.setTitle(newTitle);
		recipe.setContent(newContent);
		recipe.setCreatedAt(LocalDateTime.now());
		return recipeRepository.save(recipe);
	}
	@Transactional
	public void deleteRecipe(Long id) {
		if(recipeRepository.existsById(id)) {
			recipeRepository.deleteById(id);
		}else {
			throw new RuntimeException("Recipe not found"+id);
			}
	}

}
