package com.seatapp.repositories;

import com.seatapp.domain.User;

import java.util.List;

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
    User findByEmail(String email);

    /**
     * Get all the user from the database.
     *
     * @return all the users from the database
     * in a list
     */
    List<User> findAll();

    /**
     * Check if the user exists.
     *
     * @param email the email to search the user
     * @return a boolean to check if the user exists
     */
    Boolean existsByEmail(String email);
}
