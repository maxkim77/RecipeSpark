package com.project.RecipeSpark.Integration;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AIReviewRepository;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.AIReviewService;

@SpringBootTest
public class AIReviewServiceIntegrationTest {

    @Autowired
    private AIReviewService aiReviewService;

    @Autowired
    private AIReviewRepository aiReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGenerateAIReview_Integration() {
        // Given
        String recipeInput = "Test recipe input";
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user); // Save user first

        // When
        AIReview generatedReview = aiReviewService.generateAIReview(recipeInput, user);
        AIReview savedReview = aiReviewService.saveReview(generatedReview);

        // Then
        assertThat(savedReview.getRecipeInput()).isEqualTo(recipeInput);
        assertThat(savedReview.getUser()).isEqualTo(user);

        // Verifying if the AIReview is saved in the database
        AIReview foundReview = aiReviewRepository.findById(savedReview.getReviewId()).orElse(null);
        assertThat(foundReview).isNotNull();
        assertThat(foundReview.getRecipeInput()).isEqualTo(recipeInput);
    }
}
