package com.project.RecipeSpark.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.AIReviewRepository;
import com.project.RecipeSpark.repository.UserRepository;

@Service
public class AIReviewService {
    private final AIReviewRepository aiReviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public AIReviewService(AIReviewRepository aiReviewRepository, UserRepository userRepository) {
        this.aiReviewRepository = aiReviewRepository;
        this.userRepository = userRepository;
    }

    /**
     * AIReview 생성
     * @param recipeInput 레시피 입력
     * @param user 사용자 정보
     * @return 생성된 AIReview 객체 (저장 전)
     */
    public AIReview generateAIReview(String recipeInput, User user) {
        AIReview aiReview = new AIReview();
        aiReview.setRecipeInput(recipeInput);
        aiReview.setCreatedAt(LocalDateTime.now());
        aiReview.setUser(user);
        return aiReview; // 컨트롤러에서 저장하도록 수정
    }

    /**
     * AIReview 저장
     * @param aiReview 저장할 AIReview 객체
     * @return 저장된 AIReview 객체
     */
    public AIReview saveReview(AIReview aiReview) {
        return aiReviewRepository.save(aiReview);
    }

    /**
     * 특정 사용자의 모든 리뷰 조회
     * @param user 사용자
     * @return 리뷰 리스트
     */
    public List<AIReview> getUserReviews(User user) {
        return aiReviewRepository.findByUser(user);
    }

    /**
     * 특정 사용자의 페이지네이션 및 검색 기반 리뷰 조회
     * @param user 사용자
     * @param keyword 검색 키워드
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이지네이션된 리뷰
     */
    public Page<AIReview> getUserReviews(User user, String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 키워드가 없을 경우 전체 리뷰 반환
        if (keyword == null || keyword.isEmpty()) {
            return aiReviewRepository.findByUser(user, pageable);
        }

        // 키워드가 있는 경우 검색 결과 반환
        return aiReviewRepository.findByUserAndRecipeInputContainingIgnoreCase(user, keyword, pageable);
    }

    /**
     * 리뷰 삭제
     * @param reviewId 삭제할 리뷰 ID
     */
    public void deleteReview(Long reviewId) {
        if (!aiReviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("리뷰를 찾을 수 없습니다: " + reviewId);
        }
        aiReviewRepository.deleteById(reviewId);
    }

    /**
     * 사용자 이름으로 User 검색
     * @param username 사용자 이름
     * @return User 객체
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }
}
