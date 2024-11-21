package com.project.RecipeSpark.service;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AIReviewRepository;
import com.project.RecipeSpark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIReviewService {
    private final AIReviewRepository aiReviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public AIReviewService(AIReviewRepository aiReviewRepository, UserRepository userRepository) {
        this.aiReviewRepository = aiReviewRepository;
        this.userRepository = userRepository;
    }

    // AIReview 생성 및 저장
    public AIReview generateAIReview(String recipeInput, User user) {
        AIReview aiReview = new AIReview();
        aiReview.setRecipeInput(recipeInput);
        aiReview.setCreatedAt(LocalDateTime.now());
        aiReview.setUser(user);
        return aiReview; // 저장은 컨트롤러에서 처리하도록 수정
    }

    // AIReview 저장
    public AIReview saveReview(AIReview aiReview) {
        return aiReviewRepository.save(aiReview);
    }

    // 특정 사용자의 리뷰 목록 조회
    public List<AIReview> getUserReviews(User user) {
        return aiReviewRepository.findByUser(user);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        aiReviewRepository.deleteById(reviewId);
    }

    // 사용자 이름으로 User 검색
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }
}
