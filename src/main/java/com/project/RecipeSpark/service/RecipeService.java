package com.project.RecipeSpark.service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.RecipeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @Transactional
    public Recipe createRecipe(String title, String content, User user, MultipartFile imageFile) {
        String imagePath = saveImage(imageFile);

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setContent(content);
        recipe.setUser(user);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setImagePath(imagePath);

        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe modifyRecipe(Long id, String title, String content, MultipartFile imageFile) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));

        String imagePath = recipe.getImagePath();
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = saveImage(imageFile);
        }

        recipe.setTitle(title);
        recipe.setContent(content);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setImagePath(imagePath);

        return recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found with id: " + id);
        }
        recipeRepository.deleteById(id);
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            return null;
        }

        String uploadDir = "src/main/resources/static/images";
        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            imageFile.transferTo(filePath.toFile());
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }
}
