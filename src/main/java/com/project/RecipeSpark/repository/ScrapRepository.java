package com.project.RecipeSpark.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.RecipeSpark.domain.Recipe;
import com.project.RecipeSpark.domain.Scrap;
import com.project.RecipeSpark.domain.User;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByUserAndRecipe(User user, Recipe recipe);

    boolean existsByUserAndRecipe(User user, Recipe recipe);

    List<Scrap> findByUser(User user);
}
