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

import javax.persistence.EntityNotFoundException;

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
     * @param email the email of the user
     * @param fullName the full name of the user
     * @param password the unencoded password
     * @return a JWT token for the user to access the backend
     */
    public String login(final String email, final String fullName,
                        final String password) {
        if (!existsByEmail(email)) {
            createUser(email, fullName, password, passwordEncoder);
        }
        Authentication authentication = authenticateUser(email, password);
        return jwtUtils.generateToken(authentication);
    }

    /**
     * Authenticate the user.
     *
     * @param email the email of the user
     * @param password the unencoded password
     * @return a UsernamePasswordAuthenticationToken of the authenticated user.
     */
    private Authentication authenticateUser(final String email,
                                            final String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * Check if the user with email exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check if the user with username exists.
     *
     * @param email the email of the user
     * @return a boolean if the user exists
     */
    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "This user doesn't exist."));
    }

    /**
     * Creates a new User.
     *
     * @param email the email of the new user
     * @param fullName the full name of the new user
     * @param password the unencoded password of the new user
     * @param encoder  the encoder with which the password will be encoded
     * @return the new user
     */
    public User createUser(final String email,
                           final String fullName,
                           final String password,
                           final PasswordEncoder encoder) {
        return userRepository.save(
                new User(email, fullName, encoder.encode(password)));
    }
}
