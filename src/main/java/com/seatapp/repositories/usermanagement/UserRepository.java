package com.seatapp.repositories.usermanagement;

import com.seatapp.domain.usermanagement.User;

import java.util.Optional;

public interface UserRepository {
    /**
     * Saves the user in the database.
     *
     * @param user the user that will be saved
     *             in the database
     * @return the saved user in the database
     */
    User save(User user);

    /**
     * Finds the user by email.
     *
     * @param email the email to search the user
     * @return the optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if the user exists.
     *
     * @param email the email to search the user
     * @return a boolean to check if the user exists
     */
    Boolean existsByEmail(String email);
}
