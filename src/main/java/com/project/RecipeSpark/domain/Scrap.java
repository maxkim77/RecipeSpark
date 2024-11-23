package com.project.RecipeSpark.domain;

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
public class Scrap {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="scrap_id")
	private Long scrapId;
	
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="recipe_id",nullable=false)
	private Recipe recipe;
}
