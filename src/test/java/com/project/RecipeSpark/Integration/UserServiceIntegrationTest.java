package com.project.RecipeSpark.Integration;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.RecipeSpark.DataNotFoundException;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.UserService;

@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser_Integration() {
        // Given
        String username = "testUser";
        String email = "testuser@example.com";
        String password = "password123";

        // When
        User createdUser = userService.createUser(username, email, password);

        // Then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.getEmail()).isEqualTo(email);
        assertThat(createdUser.getPassword()).isNotEqualTo(password); // Check password is encrypted
    }

    @Test
    public void testGetUser_Integration() {
        // Given
        String username = "testUser";
        String email = "testuser@example.com";
        String password = "password123";
        userService.createUser(username, email, password); // Create a user for testing

        // When
        User foundUser = userService.getUser(username);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(username);
        assertThat(foundUser.getEmail()).isEqualTo(email);
    }


    @Test
    public void testValidatePassword_Success() {
        // Given
        String password1 = "password123";
        String password2 = "password123";

        // When
        boolean isValid = userService.validatePassword(password1, password2);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void testValidatePassword_Failure() {
        // Given
        String password1 = "password123";
        String password2 = "differentPassword";

        // When
        boolean isValid = userService.validatePassword(password1, password2);

        // Then
        assertThat(isValid).isFalse();
    }
}
