package com.seatapp.services;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
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

    @Test
    void createUserAlreadyExistsTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";

        userRepository.save(new User(email, password, fullName,  Role.ADMIN));
        when(userRepository.save(Mockito.any(User.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(email, fullName,
                        password, Role.ADMIN));
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
                .thenReturn(new User(email, password, fullName, Role.ADMIN));

        // Act
        User user = userService.getByEmail(email);

        // Assert
        assertEquals(email, user.getEmail());
        assertEquals(fullName, user.getFullName());
        assertEquals(password, user.getPassword());
    }

    @Test
    void getAllUsers() {
        // Arrange
        User user1 = new User("User1", "PW",
                "User1", Role.ADMIN);

        User user2 = new User("User2", "PW",
                "User2", Role.ADMIN);

        given(userRepository.findAll()).willReturn(List.of(user1, user2));

        // Act
        List<User> users = userService.getAll();

        // Assert
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    void changeUserRole() {
        // Arrange
        User existingUser = new User("User3", "PW",
                "User3", Role.USER);

        User userDto = new User("User4", "PW",
                "User4", Role.ADMIN);

        given(userRepository.findByEmail("User4")).willReturn(existingUser);

        // Act
        userService.changeUserRole(userDto, "Louiske");

        // Assert
        assertNotEquals(existingUser.getRole(), Role.USER);
    }

    @Test
    void changeUserRoleFromYourSelf() {
        // Arrange
        User existingUser = new User("User5", "PW",
                "User5", Role.ADMIN);

        given(userRepository.findByEmail("User5")).willReturn(existingUser);

        // Act
        assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserRole(existingUser, "User5"));

        // Assert
        assertNotEquals(existingUser.getRole(), Role.USER);
    }
}
