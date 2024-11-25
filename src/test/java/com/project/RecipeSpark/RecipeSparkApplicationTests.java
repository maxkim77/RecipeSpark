package com.project.RecipeSpark;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.service.RecipeService;

@SpringBootTest
class RecipeSparkApplicationTests {

    @Autowired
    private RecipeService recipeService;

    @Test
    void contextLoads() {
        // 애플리케이션 컨텍스트가 제대로 로딩되면 recipeService가 자동으로 주입됩니다.
        assertThat(recipeService).isNotNull();
    }
}
