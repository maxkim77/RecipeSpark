package com.project.RecipeSpark.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;

@Repository
public interface AIReviewRepository extends JpaRepository<AIReview, Long> {
    List<AIReview> findByUser(User user);
    Page<AIReview> findByUser(User user, Pageable pageable);
    Page<AIReview> findByUserAndRecipeInputContainingIgnoreCase(User user, String keyword, Pageable pageable);
}
