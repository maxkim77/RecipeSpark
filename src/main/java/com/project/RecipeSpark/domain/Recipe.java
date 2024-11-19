package com.project.RecipeSpark.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private LocalDateTime createdAt;
    
    @Column(length = 255)
    private String imagePath; // 이미지 경로


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
