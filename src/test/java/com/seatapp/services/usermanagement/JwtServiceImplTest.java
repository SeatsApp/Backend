package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.JwtUserDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        String username = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                Mockito.spy(new UsernamePasswordAuthenticationToken(username,
                        password));
        Mockito.when(authentication.getPrincipal())
                .thenReturn(new JwtUserDetails(username, email, password,
                        new ArrayList<>()));

        // Act
        String jwt = jwtService.generateToken(authentication);

        // Assert
        assertEquals(username, jwtService.getEmailFromJwtToken(jwt));
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
