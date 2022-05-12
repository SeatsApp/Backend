package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.usermanagement.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceImplTest {
    /**
     * Represents the user repository.
     */
    @Mock
    private UserRepository userRepository;
    /**
     * Represents the user service.
     */
    @InjectMocks
    private UserServiceImpl userService;
    /**
     * Represents the password encoder.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createUserTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));

        // Act
        User user = userService.createUser(email, fullName, password,
                passwordEncoder);

        // Assert
        assertEquals(fullName, user.getFullName());
        assertEquals(email, user.getEmail());
        assertNotEquals(password, user.getPassword());
    }

    @Test
    void createUserAlreadyExistsTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        userRepository.save(new User(email, fullName, password));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(email, fullName, password,
                        passwordEncoder));
    }
}
