package com.taxiapp;

import com.taxiapp.model.User;
import com.taxiapp.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void testValidLogin() {
        User user = authService.login("user@test.com", "password123");
        assertNotNull(user);
        assertEquals("user@test.com", user.getEmail());
    }

    @Test
    public void testInvalidLogin() {
        User user = authService.login("invalid@test.com", "wrongpassword");
        assertNull(user); // Invalid credentials should return null
    }

    @Test
    public void testRegisterNewUser() {
        User user = authService.register("newuser@test.com", "password123", "New User", "1234567890", "user");
        assertNotNull(user);
        assertEquals("newuser@test.com", user.getEmail());
    }

    @Test
    public void testRegisterDuplicateEmail() {
        authService.register("duplicate@test.com", "password123", "User 1", "1234567890", "user");
        User duplicate = authService.register("duplicate@test.com", "password123", "User 2", "0987654321", "user");
        assertNull(duplicate); // Duplicate email should not be allowed
    }

    @Test
    public void testLoginWithEmptyPassword() {
        User user = authService.login("user@test.com", "");
        assertNull(user); // Server should reject, but currently accepts
    }

    @Test
    public void testLoginWithMalformedEmail() {
        User user = authService.login("invalid-email", "password123");
        assertNull(user); // Server should reject, but currently accepts
    }

    @Test
    void testValidateCredentials() {
        // Arrange
        // Mock dependencies and inputs
        // Act
        // Call validateCredentials
        // Assert
        // Validate the result
    }
}
