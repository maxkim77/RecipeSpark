package com.project.RecipeSpark.domain;

import com.project.RecipeSpark.security.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	@Column(unique=true)
	private String username;
	private String password;
	@Column(unique=true)
	private String email;
	 @Enumerated(EnumType.STRING)
	    private UserRole role; // 이 필드가 있어야 합니다.

}
