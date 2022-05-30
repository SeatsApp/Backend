package com.seatapp.services;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    /**
     * Represents the user repository.
     */
    private final UserRepository userRepository;

    /**
     * Creates a user service with the following parameters.
     *
     * @param userRepository        the user repository
     */
    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Check if the user with email exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check if the user with username exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
    @Override
    public User getByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Gets all the users from database.
     *
     * @return a list of users
     */
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Creates a new User.
     *
     * @param email    the email of the new user
     * @param fullName the full name of the new user
     * @param password the unencoded password of the new user
     * @param role the role of the new user
     * @return the new user
     */
    @Override
    public User createUser(final String email,
                           final String fullName,
                           final String password,
                           final Role role) {
        return userRepository.save(
                new User(email, password, fullName, role));
    }

    /**
     * Changes the role of a user.
     * @param user the to be changed user and the new role.
     * @param email the email of the user who wants to change the user.
     * @return the user with updated role.
     */
    @Override
    public User changeUserRole(final User user,
                               final String email) {
        if (user.getEmail().equals(email)) {
            throw new IllegalArgumentException(
                    "A user can't change it's own role.");
        }
        User foundUser = getByEmail(user.getEmail());
        foundUser.setRole(user.getRole());
        return userRepository.save(foundUser);
    }
}
