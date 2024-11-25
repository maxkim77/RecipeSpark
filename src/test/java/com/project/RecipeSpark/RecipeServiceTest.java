package com.project.RecipeSpark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.RecipeRepository;
import com.project.RecipeSpark.service.RecipeService;

public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRecipe() {
        // Given
        String title = "Test Recipe";
        String content = "Test content for the recipe.";
        User user = new User();
        user.setUsername("testUser");

        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(title);
        newRecipe.setContent(content);
        newRecipe.setUser(user);

        when(recipeRepository.save(any(Recipe.class))).thenReturn(newRecipe);

        // When
        Recipe createdRecipe = recipeService.createRecipe(title, content, user, null);

        // Then
        assertThat(createdRecipe.getTitle()).isEqualTo(title);
        assertThat(createdRecipe.getContent()).isEqualTo(content);
        assertThat(createdRecipe.getUser()).isEqualTo(user);
    }

    @Test
    public void testGetRecipeById() {
        // Given
        Long recipeId = 1L;
        Recipe recipe = new Recipe();
        recipe.setReviewId(recipeId);
        recipe.setTitle("Sample Recipe");

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // When
        Optional<Recipe> foundRecipe = recipeService.getRecipeById(recipeId);

        // Then
        assertThat(foundRecipe).isPresent();
        assertThat(foundRecipe.get().getTitle()).isEqualTo("Sample Recipe");
    }
}
