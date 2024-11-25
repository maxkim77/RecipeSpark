package com.project.RecipeSpark.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.RecipeRepository;
import com.project.RecipeSpark.service.RecipeService;

@SpringBootTest
public class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void testCreateRecipe_Integration() {
        // Given
        String title = "Test Recipe";
        String content = "This is a test recipe content.";
        User user = new User();
        user.setUsername("testUser");

        // When
        Recipe createdRecipe = recipeService.createRecipe(title, content, user, null);

        // Then
        assertThat(createdRecipe.getTitle()).isEqualTo(title);
        assertThat(createdRecipe.getContent()).isEqualTo(content);

        // Verifying if the recipe is saved in the database
        Recipe foundRecipe = recipeRepository.findById(createdRecipe.getReviewId()).orElse(null);
        assertThat(foundRecipe).isNotNull();
        assertThat(foundRecipe.getTitle()).isEqualTo(title);
    }
}

