package com.project.RecipeSpark.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

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

        // 파일 확장자 추출
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            throw new RuntimeException("Invalid file name. File must have an extension.");
        }

        // 파일 이름 생성 (UUID)
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // 저장 경로 생성
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

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
    /**
     * 페이징된 레시피 목록 조회 (검색 포함)
     */
    public Page<Recipe> getPaginatedRecipes(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (keyword == null || keyword.isEmpty()) {
            return recipeRepository.findAll(pageable);
        }
        return recipeRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }
}
