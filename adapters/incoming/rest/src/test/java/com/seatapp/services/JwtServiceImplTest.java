package com.seatapp.services;

import com.seatapp.usermanagement.services.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtServiceImplTest {
    /**
     * Representing the JWT utilities.
     */
    @Autowired
    private JwtServiceImpl jwtService;

    @Test
    void generateToken() {
        // Arrange
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                Mockito.spy(new UsernamePasswordAuthenticationToken(email,
                        password, List.of(
                        new SimpleGrantedAuthority(
                                "ADMIN"))));
        Mockito.when(authentication.getName())
                .thenReturn(email);

        // Act
        String jwt = jwtService.generateToken(authentication);

        // Assert
        assertEquals(email, jwtService.getEmailFromJwtToken(jwt));
    }

    @Test
    void validateTokenWhenEmptyTokenTest() {
        // Act
        boolean validation = jwtService.validateJwtToken("");

        // Assert
        assertFalse(validation);
    }

    @Test
    void validateTokenWhenInvalidSignatureTokenTest() {
        // Act
        boolean validation = jwtService.validateJwtToken("eyJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJUaG9tYXMgVmFuIERlIFdhbGxlIiwiZXhwIjoxNjUyMDc5ODg3"
                + "LCJpYXQiOjE2NTE5OTM0ODd9.BPwt8xEyEkBUapIQKYpJP"
                + "dDt80khWQzB7Nm7TsIJhbvjJ5msbRusifMHEzHkc4mDo8Ih0W9g");

        // Assert
        assertFalse(validation);
    }

    @Test
    void validateTokenExpiredTest() {
        // Act
        boolean validation = jwtService.validateJwtToken(
                "eyJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJUaG9tYXMgVmFuIERlIF"
                + "dhbGxlIiwiZXhwIjoxNjUyMDc5ODg3"
                + "LCJpYXQiOjE2NTE5OTM0ODd9.cJFz53"
                + "0GcYoyIyBPwt8xEyEkBUapIQKYpJP"
                + "dDt80khWQzB7Nm7TsIJhbvjJ5msbRusifMHEzHkc4mDo8Ih0W9g");

        // Assert
        assertFalse(validation);
    }
}
