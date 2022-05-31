package com.seatapp.repositories;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class UserRepositoryTests {
    /**
     * A seat object used in testing.
     */
    private static final User VALID_USER = new User("Test@test.be",
            "TestUser", "TestUser", Role.ADMIN);

    /**
     * The userRepositoryImplementation.
     */
    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    @Transactional
    void saveUser() {
        //Act
        User savedUser = userRepository.save(VALID_USER);

        //Assert
        assertEquals(savedUser.getEmail(), VALID_USER.getEmail());
        assertEquals(savedUser.getFullName(), VALID_USER.getFullName());
        assertEquals(savedUser.getRole(), VALID_USER.getRole());
    }

    @Test
    @Transactional
    void findByEmail() {
        //Arrange
        User savedUser = userRepository.save(VALID_USER);

        //Act
        User foundUser = userRepository.findByEmail(savedUser.getEmail());

        //Assert
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
        assertEquals(savedUser.getRole(), foundUser.getRole());
        assertEquals(savedUser.getFullName(), foundUser.getFullName());
    }

    @Test
    @Transactional
    void findUserByInvalidId() {
        assertThrows(EntityNotFoundException.class,
                () -> userRepository.findByEmail("NotValid"));
    }

    @Test
    @Transactional
    void existsByEmail() {
        //Arrange
        User savedUser = userRepository.save(VALID_USER);

        //Act
        boolean found = userRepository.existsByEmail(savedUser.getEmail());

        //Assert
        assertTrue(found);
    }

    @Test
    @Transactional
    void notExistByEmail() {
        //Act
        boolean found = userRepository.existsByEmail("WrongEmail");

        //Assert
        assertFalse(found);
    }

    @Test
    @Transactional
    void findAll() {
        //Arrange
        User savedUser = userRepository.save(VALID_USER);
        savedUser.setEmail("Testje");
        userRepository.save(savedUser);

        //Act
        List<User> foundUser = userRepository.findAll();

        //Assert
        assertEquals(2, foundUser.size());
    }
}
