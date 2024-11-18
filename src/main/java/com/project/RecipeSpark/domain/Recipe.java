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
public class Recipe {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long reviewId;
	@Column(length=255)
	private String title;
	@Column(columnDefinition="TEXT")
	private String content;
	
    private LocalDateTime createdAt;
    
    @Column(length = 255)
    private String imagePath; // 이미지 경로

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
}
