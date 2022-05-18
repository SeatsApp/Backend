package com.seatapp.usermanagement.services;

import com.seatapp.domain.Role;
import com.seatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginServiceImpl implements LoginService {
    /**
     * Processes authentication requests.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * The userservice used to create the user.
     */
    private final UserService userService;

    /**
     * Used for encoding the authentication password.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a login service with the following parameters.
     *  @param authenticationManager the authentication manager
     *                              to authenticate the logged in user
     * @param userService the userservice used to create the user
     * @param passwordEncoder       the used encoder for encoding the passwords
     */
    @Autowired
    public LoginServiceImpl(final AuthenticationManager authenticationManager,
                           final UserService userService,
                           final PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login to the application.
     *
     * @param email    the email of the user
     * @param fullName the full name of the user
     * @param password the unencoded password
     * @param role the role of the user
     * @return the authentication of the user that logged in.
     */
    @Override
    public Authentication login(final String email, final String fullName,
                                final String password, final Role role) {
        if (!userService.existsByEmail(email)) {
            userService.createUser(email, fullName,
                    passwordEncoder.encode(password), role);
        }
        return authenticateUser(email, password);
    }

    /**
     * Authenticate the user.
     *
     * @param email    the email of the user
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
}
