package com.seatapp.services;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;

import java.util.List;

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
     * Gets all the users from database.
     *
     * @return a list of users
     */
    List<User> getAll();

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
    /**
     * Changes the role of a user.
     * @param user the to be changed user and the new role.
     * @param email the email of the user who wants to change the user.
     * @return the user with updated role.
     */
     User changeUserRole(User user, String email);
}
