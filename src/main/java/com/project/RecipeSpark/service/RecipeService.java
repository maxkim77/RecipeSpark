package com.project.RecipeSpark.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 레시피 ID로 조회
     */
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    /**
     * 새 레시피 생성
     */
    @Transactional
    public Recipe createRecipe(String title, String content, User user, MultipartFile imageFile) {
        String imagePath = null;

        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = saveImage(imageFile);
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setContent(content);
        recipe.setUser(user);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setImagePath(imagePath);

        return recipeRepository.save(recipe);
    }

    /**
     * 레시피 수정
     */
    @Transactional
    public Recipe modifyRecipe(Long id, String title, String content, MultipartFile imageFile) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with ID: " + id));

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

    /**
     * 레시피 삭제
     */
    @Transactional
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found with ID: " + id);
        }
        recipeRepository.deleteById(id);
    }

    /**
     * 이미지 저장
     */
    private String saveImage(MultipartFile imageFile) {
        // MIME 타입 검증
        String mimeType = imageFile.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new RuntimeException("Invalid file type. Only image files are allowed.");
        }

        // 저장 디렉토리 설정
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

        // 파일 이름을 UUID로 저장 (한글/특수문자 제거)
        String fileExtension = imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = Paths.get(uploadDir, fileName);

        try {
            // 디렉토리가 없으면 생성
            Files.createDirectories(filePath.getParent());

            // 파일 저장
            imageFile.transferTo(filePath.toFile());
            return "/uploads/" + fileName; // URL 경로 반환
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file.", e);
        }
    }


    /**
     * 모든 레시피 조회
     */
    public List<Recipe> getRecipe() {
        return recipeRepository.findAll();
    }
}
