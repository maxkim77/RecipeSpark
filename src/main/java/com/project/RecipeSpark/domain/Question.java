package com.project.RecipeSpark.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answerList;

    
    private int voteCount; // 추천 수

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuestionVoter> voters = new HashSet<>(); // 추천한 사용자와의 관계

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
}
