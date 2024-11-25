package com.project.RecipeSpark;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AIReviewRepository;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.AIReviewService;

public class AIReviewServiceTest {

    @Mock
    private AIReviewRepository aiReviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AIReviewService aiReviewService;

    @Mock
    private User user;

    @Mock
    private AIReview aiReview;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateAIReview() {
        // Given
        String recipeInput = "Test recipe input";

        // When
        AIReview generatedReview = aiReviewService.generateAIReview(recipeInput, user);

        // Then
        assertThat(generatedReview.getRecipeInput()).isEqualTo(recipeInput);
        assertThat(generatedReview.getUser()).isEqualTo(user);
        assertThat(generatedReview.getCreatedAt()).isNotNull();
    }

    @Test
    public void testSaveReview() {
        // Given
        when(aiReviewRepository.save(any(AIReview.class))).thenReturn(aiReview);

        // When
        AIReview savedReview = aiReviewService.saveReview(aiReview);

        // Then
        assertThat(savedReview).isNotNull();
        verify(aiReviewRepository, times(1)).save(aiReview);
    }

    @Test
    public void testGetUserReviews() {
        // Given
        List<AIReview> reviews = List.of(aiReview);
        when(aiReviewRepository.findByUser(user)).thenReturn(reviews);

        // When
        List<AIReview> userReviews = aiReviewService.getUserReviews(user);

        // Then
        assertThat(userReviews).isNotEmpty();
        assertThat(userReviews.size()).isEqualTo(1);
        verify(aiReviewRepository, times(1)).findByUser(user);
    }

    @Test
    public void testDeleteReview() {
        // Given
        Long reviewId = 1L;
        when(aiReviewRepository.existsById(reviewId)).thenReturn(true);
        doNothing().when(aiReviewRepository).deleteById(reviewId);

        // When
        aiReviewService.deleteReview(reviewId);

        // Then
        verify(aiReviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    public void testGetUserByUsername() {
        // Given
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User foundUser = aiReviewService.getUserByUsername(username);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
        verify(userRepository, times(1)).findByUsername(username);
    }

}
