package com.project.RecipeSpark.repository;

import com.project.RecipeSpark.domain.AIReview;
import com.project.RecipeSpark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIReviewRepository extends JpaRepository<AIReview, Long> {
    List<AIReview> findByUser(User user);
}
