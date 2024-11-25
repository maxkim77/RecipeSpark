package com.project.RecipeSpark.System;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AIReviewRepository;
import com.project.RecipeSpark.repository.QuestionRepository;
import com.project.RecipeSpark.repository.RecipeRepository;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.AIReviewService;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.RecipeService;
import com.project.RecipeSpark.service.UserService;

@SpringBootTest
public class ApplicationSystemTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private AIReviewService aiReviewService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private AIReviewRepository aiReviewRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create a test user
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123"); // Password will be encrypted
        userRepository.save(testUser);
    }

    // 시스템 테스트: 레시피 생성, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testCreateRecipeSystem() {
        // Given
        String title = "Test Recipe";
        String content = "This is a test recipe content.";

        // When
        Recipe createdRecipe = recipeService.createRecipe(title, content, testUser, null);

        // Then
        assertThat(createdRecipe.getTitle()).isEqualTo(title);
        assertThat(createdRecipe.getContent()).isEqualTo(content);

        // Verifying if the recipe is saved in the database
        Recipe foundRecipe = recipeRepository.findById(createdRecipe.getReviewId()).orElse(null);
        assertThat(foundRecipe).isNotNull();
        assertThat(foundRecipe.getTitle()).isEqualTo(title);
    }

    // 시스템 테스트: AI 리뷰 생성, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testCreateAIReviewSystem() {
        // Given
        String recipeInput = "Test AI recipe input";

        // When
        AIReview generatedReview = aiReviewService.generateAIReview(recipeInput, testUser);
        AIReview savedReview = aiReviewService.saveReview(generatedReview);

        // Then
        assertThat(savedReview.getRecipeInput()).isEqualTo(recipeInput);
        assertThat(savedReview.getUser()).isEqualTo(testUser);

        // Verifying if the AIReview is saved in the database
        AIReview foundReview = aiReviewRepository.findById(savedReview.getReviewId()).orElse(null);
        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getRecipeInput()).isEqualTo(recipeInput);
    }

    // 시스템 테스트: 질문 생성, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testCreateQuestionSystem() {
        // Given
        String questionTitle = "How to make a perfect cake?";
        String questionContent = "Looking for tips on baking the best cake.";

        // When
        questionService.createQuestion(questionTitle, questionContent, testUser);

        // Then
        Question createdQuestion = questionRepository.findByTitle(questionTitle);
        assertThat(createdQuestion).isNotNull();
        assertThat(createdQuestion.getTitle()).isEqualTo(questionTitle);
    }

    // 시스템 테스트: 레시피 삭제, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testDeleteRecipeSystem() {
        // Given
        String title = "Recipe to delete";
        String content = "Content of the recipe to delete.";
        Recipe createdRecipe = recipeService.createRecipe(title, content, testUser, null);

        // When
        recipeService.deleteRecipe(createdRecipe.getReviewId());

        // Then
        assertThat(recipeRepository.findById(createdRecipe.getReviewId())).isEmpty();
    }

    // 시스템 테스트: 질문 삭제, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testDeleteQuestionSystem() {
        // Given
        String title = "Question to delete";
        Question question = new Question();
        question.setTitle(title);
        questionRepository.save(question);

        // When
        questionService.deleteQuestion(question);

        // Then
        Question deletedQuestion = questionRepository.findByTitle(title);
        assertThat(deletedQuestion).isNull();
    }

    // 시스템 테스트: 회원 조회, 데이터베이스 확인
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testGetUserSystem() {
        // Given
        String username = "testUser";

        // When
        User foundUser = userService.getUser(username);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(username);
    }
}
