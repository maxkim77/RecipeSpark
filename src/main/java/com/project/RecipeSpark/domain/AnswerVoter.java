package com.project.RecipeSpark.domain;

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
public class AnswerVoter {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long answerVoteId;
	
	@ManyToOne
	@JoinColumn(name="questionId")
	private Answer answer;
	
	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

}
