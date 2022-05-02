package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.usermanagement.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    /**
     * Represents the user repository.
     */
    private final UserRepository userRepository;
    /**
     * Processes authentication requests.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * JWT creator and authenticator.
     */
    private final JwtUtils jwtUtils;

    /**
     * Used for encoding the authentication password.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a user service with the following parameters.
     *
     * @param userRepository        the user repository
     * @param authenticationManager the authentication manager
     *                              to authenticate the logged in user
     * @param jwtUtils              the jwt utils to authenticate and
     *                              generate a JWT token
     * @param passwordEncoder       the used encoder for encoding the passwords
     */
    @Autowired
    public UserService(final UserRepository userRepository,
                       final AuthenticationManager authenticationManager,
                       final JwtUtils jwtUtils,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login to the application.
     *
     * @param username the full name of the user
     * @param email    the email of the user
     * @param password the unencoded password
     * @return a JWT token for the user to access the backend
     */
    public String login(final String username, final String email,
                        final String password) {
        if (!existsByUsername(username)) {
            createUser(username, email, password, passwordEncoder);
        }
        Authentication authentication = authenticateUser(username, password);
        return jwtUtils.generateToken(authentication);
    }

    /**
     * Authenticate the user.
     *
     * @param username the username of the user
     * @param password the unencoded password
     * @return a UsernamePasswordAuthenticationToken of the authenticated user.
     */
    private Authentication authenticateUser(final String username,
                                            final String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * Check if the user with username exists.
     *
     * @param username the username of the user
     * @return a boolean if the user exists
     */
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Creates a new User.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the unencoded password of the new user
     * @param encoder  the encoder with which the password will be encoded
     * @return the new user
     */
    public User createUser(final String username,
                           final String email,
                           final String password,
                           final PasswordEncoder encoder) {
        return userRepository.save(
                new User(username, email, encoder.encode(password)));
    }
}
