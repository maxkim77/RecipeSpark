package com.project.RecipeSpark.Integration;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.QuestionRepository;
import com.project.RecipeSpark.service.QuestionService;

@SpringBootTest
public class QuestionServiceIntegrationTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testCreateQuestion_Integration() {
        // Given
        String title = "Test Question";
        String content = "This is a test question content.";
        User user = new User();
        user.setUsername("testUser");

        // When
        questionService.createQuestion(title, content, user);

        // Then
        Question foundQuestion = questionRepository.findByTitle(title);
        assertThat(foundQuestion).isNotNull();
        assertThat(foundQuestion.getTitle()).isEqualTo(title);
    }

    @Test
    public void testDeleteQuestion_Integration() {
        // Given
        String title = "Test Question to delete";
        Question question = new Question();
        question.setTitle(title);
        questionRepository.save(question);

        // When
        questionService.deleteQuestion(question);

        // Then
        Question deletedQuestion = questionRepository.findByTitle(title);
        assertThat(deletedQuestion).isNull();
    }
}
