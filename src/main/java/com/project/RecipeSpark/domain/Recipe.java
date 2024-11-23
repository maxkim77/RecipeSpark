package com.project.RecipeSpark.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId; // 레시피 ID

    @Column(length = 255, nullable = false)
    private String title; // 레시피 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 레시피 내용

    private LocalDateTime createdAt; // 생성 날짜

    @Column(length = 255)
    private String imagePath; // 이미지 경로

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 레시피 작성자

    // 댓글 리스트와 연관관계 추가
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // 레시피에 연결된 댓글 목록
}
