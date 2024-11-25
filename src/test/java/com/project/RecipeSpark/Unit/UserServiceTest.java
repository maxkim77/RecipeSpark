package com.project.RecipeSpark.Unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.RecipeSpark.DataNotFoundException;
import com.project.RecipeSpark.domain.User;
import com.project.RecipeSpark.repository.UserRepository;
import com.project.RecipeSpark.service.UserService;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        // Given
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("encryptedPassword");

        // Mock Password Encoder
        when(passwordEncoder.encode(password)).thenReturn("encryptedPassword");

        // When
        User createdUser = userService.createUser(username, email, password);

        // Then
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(createdUser.getEmail()).isEqualTo(email);
        assertThat(createdUser.getPassword()).isEqualTo("encryptedPassword");
    }

    @Test
    public void testGetUser_Success() {
        // Given
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        // Mock Repository
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getUser(username);

        // Then
        assertThat(foundUser.getUsername()).isEqualTo(username);
    }

    @Test
    public void testGetUser_NotFound() {
        // Given
        String username = "nonExistingUser";

        // Mock Repository
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DataNotFoundException.class, () -> userService.getUser(username));
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
