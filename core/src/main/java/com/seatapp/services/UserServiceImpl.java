package com.seatapp.services;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
     * Creates a new User.
     *
     * @param email    the email of the new user
     * @param fullName the full name of the new user
     * @param password the unencoded password of the new user
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
}
