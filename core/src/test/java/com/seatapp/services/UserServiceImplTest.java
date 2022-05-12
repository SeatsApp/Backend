package com.seatapp.services;

import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {
    /**
     * Represents the user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;
    /**
     * Represents the user service.
     */
    @Autowired
    private UserServiceImpl userService;
    /**
     * Represents the password encoder.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Represents the authentication manager.
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void loginUserDoesNotExistTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, password);

        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);

        // Act
        Authentication receivedAuthentication =
                userService.login(email, fullName, password);

        // Assert
        assertEquals(email, receivedAuthentication.getName());
    }

    @Test
    void loginUserDoesExistTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, password);

        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);

        // Act
        Authentication receivedAuthentication =
                userService.login(email, fullName, password);

        // Assert
        assertEquals(email, receivedAuthentication.getName());
    }

    @Test
    void createUserAlreadyExistsTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        userRepository.save(new User(email, fullName, password));
        when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(email, fullName, password,
                        passwordEncoder));
    }

    @Test
    void getByEmailTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));
        when(userRepository.findByEmail(email))
                .thenReturn(new User(email, fullName, password));

        // Act
        User user = userService.getByEmail(email);

        // Assert
        assertEquals(email, user.getEmail());
        assertEquals(fullName, user.getFullName());
        assertEquals(password, user.getPassword());
    }
}
