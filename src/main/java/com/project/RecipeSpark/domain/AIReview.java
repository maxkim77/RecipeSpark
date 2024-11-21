package com.project.RecipeSpark.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AIReview {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long reviewId;

    @Column(columnDefinition="TEXT")
    private String recipeInput;

    @Column(columnDefinition="TEXT")
    private String reviewResult;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
}
