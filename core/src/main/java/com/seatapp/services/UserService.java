package com.seatapp.services;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;

public interface UserService {
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
     * @param role the role of the user.
     * @return the new user
     */
     User createUser(String email,
                     String fullName,
                     String password,
                     Role role);
}
