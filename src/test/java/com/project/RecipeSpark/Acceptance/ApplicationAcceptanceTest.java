package com.project.RecipeSpark.Acceptance;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.RecipeRepository;
import com.project.RecipeSpark.repository.UserRepository;

@SpringBootTest
public class ApplicationAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private String baseUrl = "http://localhost:8080";  // Assuming the server runs on localhost:8080

    private User testUser;

    @BeforeEach
    public void setup() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123"); // Password will be encrypted
        userRepository.save(testUser);
    }

    // 인수 테스트: 회원가입
    @Test
    public void testUserRegistration() {
        String url = baseUrl + "/api/auth/register";
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");

        ResponseEntity<User> response = restTemplate.postForEntity(url, newUser, User.class);

        // Verify the response
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("newUser");
    }

    // 인수 테스트: 로그인
    @Test
    public void testUserLogin() {
        String url = baseUrl + "/api/auth/login";
        User loginUser = new User();
        loginUser.setUsername("testUser");
        loginUser.setPassword("password123");

        ResponseEntity<String> response = restTemplate.postForEntity(url, loginUser, String.class);

        // Verify that the login response contains a JWT token
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("Bearer"); // JWT token
    }

    // 인수 테스트: 게시글 작성
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testCreateRecipe() {
        String url = baseUrl + "/api/recipes";
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle("Test Recipe");
        newRecipe.setContent("This is a test recipe");

        ResponseEntity<Recipe> response = restTemplate.postForEntity(url, newRecipe, Recipe.class);

        // Verify the response
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Recipe");
    }

    // 인수 테스트: 게시글 조회
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testGetRecipeById() {
        // Create a new recipe to test
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle("Recipe to view");
        newRecipe.setContent("Content of the recipe");
        recipeRepository.save(newRecipe);

        String url = baseUrl + "/api/recipes/" + newRecipe.getReviewId();

        ResponseEntity<Recipe> response = restTemplate.exchange(url, HttpMethod.GET, null, Recipe.class);

        // Verify the response
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getTitle()).isEqualTo("Recipe to view");
    }

    // 인수 테스트: 게시글 삭제
    @Test
    @WithMockUser(username = "testUser", password = "password123", roles = "USER")
    public void testDeleteRecipe() {
        // Create a new recipe to delete
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle("Recipe to delete");
        newRecipe.setContent("Content of the recipe");
        recipeRepository.save(newRecipe);

        String url = baseUrl + "/api/recipes/" + newRecipe.getReviewId();

        // Delete the recipe
        restTemplate.delete(url);

        // Verify the recipe was deleted
        try {
            restTemplate.getForEntity(url, Recipe.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode().value()).isEqualTo(404); // Not Found
        }
    }
}
