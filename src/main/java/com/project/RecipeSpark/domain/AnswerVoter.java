package com.project.RecipeSpark.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class AnswerVoter {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	
	@ManyToOne
	@JoinColumn(name="questionId")
	private Answer answer;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
