package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.usermanagement.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceTest {
    /**
     * Represents the user repository.
     */
    @MockBean
    private UserRepository userRepository;
    /**
     * Represents the user service.
     */
    @Autowired
    private UserService userService;
    /**
     * Represents the password encoder.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createUserTest() {
        // Arrange
        String username = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> {
                    User user = i.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        // Act
        User user = userService.createUser(username, email, password,
                passwordEncoder);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertNotEquals(password, user.getPassword());
        assertEquals(1L, user.getId());
    }

    @Test
    void createUserAlreadyExistsTest() {
        // Arrange
        String username = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        userRepository.save(new User(username, email, password));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.createUser(username, email, password,
                            passwordEncoder);
                });
    }
}
