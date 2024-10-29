package com.project.RecipeSpark.domain;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer questionId;
	
	@Column(length=200)
	private String title;
	
	@Column(columnDefinition="TEXT")
	private String content;
	
	private int voteCount;  // 투표 수를 기록하는 필드
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@OneToMany(mappedBy="question",cascade=CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answerList;
	
	@ManyToOne
	@JoinColumn(name="authorId")
	private User authorId;
	
}
