package com.project.RecipeSpark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByusername(String username);
	Optional<User> findByEmail(String email);
}
