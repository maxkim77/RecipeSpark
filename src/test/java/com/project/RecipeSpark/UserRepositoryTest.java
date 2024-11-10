package com.project.RecipeSpark;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 내장 DB 사용 방지
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUserWithSetters() {
        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@example.com");

        User savedUser = userRepository.save(user);
        assertThat(savedUser.getUserId()).isNotNull();
    }
}