package com.project.RecipeSpark.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class QuestionVoter {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long questionVoteId;
	
	@ManyToOne
	@JoinColumn(name="questionId")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
