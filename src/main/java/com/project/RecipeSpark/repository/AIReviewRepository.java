package com.project.RecipeSpark.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.RecipeSpark.domain.AIReview;

public interface AIReviewRepository extends JpaRepository<AIReview, Long> {
	// 특정 사용자가 요청한 AI 리뷰조
	List<AIReview> findByUserUserId(Long userId);

}
