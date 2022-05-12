package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
    /**
     * Login to the application.
     *
     * @param email the email of the user
     * @param fullName the full name of the user
     * @param password the unencoded password
     * @return a JWT token for the user to access the backend
     */
     String login(String email,
                         String fullName, String password);

    /**
     * Check if the user with email exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
     boolean existsByEmail(String email);

    /**
     * Check if the user with username exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
     User getByEmail(String email);

    /**
     * Creates a new User.
     *
     * @param email the email of the new user
     * @param fullName the full name of the new user
     * @param password the unencoded password of the new user
     * @param encoder  the encoder with which the password will be encoded
     * @return the new user
     */
     User createUser(String email,
                     String fullName,
                     String password,
                     PasswordEncoder encoder);
}
