package com.project.RecipeSpark.Integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.domain.Question;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.QuestionRepository;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.QuestionService;
import com.project.RecipeSpark.service.UserService;

@SpringBootTest
public class QuestionServiceIntegrationTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreate100Questions() {
        // Given: 고유한 유저 생성
        String username = "testUser_" + System.currentTimeMillis();
        String email = username + "@example.com";
        String password = "password123";

        User user = userService.createUser(username, email, password);

        // When: 100개의 질문 생성
        for (int i = 1; i <= 100; i++) {
            String title = "Test Question " + i;
            String content = "This is the content for test question number " + i;
            questionService.createQuestion(title, content, user);
        }

        // Then: 데이터베이스에 질문이 생성되었는지 확인
        long questionCount = questionRepository.count();
        assertThat(questionCount).isGreaterThanOrEqualTo(100); // 예상된 개수 이상인지 확인
    }
}
