package com.seatapp.usermanagement.services;

import com.seatapp.domain.Role;
import com.seatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     *
     * @param authenticationManager the authentication manager
     *                              to authenticate the logged in user
     * @param userService           the userservice used to create the user
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
     * Log the user in to the application.
     *
     * @param principal the user trying to log in
     * @return the authentication of the user
     */
    @Override
    public Authentication login(final OAuth2User principal) {
        String fullName = principal.getName();
        String email = principal.getAttribute("preferred_username");
        Role role = getRole(principal);

        return setupUser(email,
                fullName, email, role);
    }

    /**
     * Sets up the user account.
     *
     * @param email    the email of the user
     * @param fullName the full name of the user
     * @param password the unencoded password
     * @param role     the role of the user
     * @return the authentication of the user that logged in.
     */
    private Authentication setupUser(final String email, final String fullName,
                                     final String password, final Role role) {
        if (!userService.existsByEmail(email)) {
            userService.createUser(email, fullName,
                    passwordEncoder.encode(password), role);
        }
        return authenticateUser(email, password, role);
    }

    /**
     * Authenticate the user.
     *
     * @param email    the email of the user
     * @param password the unencoded password
     * @param role     the role of the user
     * @return a UsernamePasswordAuthenticationToken of the authenticated user.
     */
    private Authentication authenticateUser(final String email,
                                            final String password,
                                            final Role role) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, password,
                        List.of(new SimpleGrantedAuthority(
                                role.name())));

        authenticationManager.authenticate(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * Set the cookie to nothing so that the login
     * with after the first time does not crash.
     *
     * @param response the response that will be sent back to the user.
     */
    @Override
    public void setCookieJSessionId(final HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    /**
     * Get role from logged in user.
     *
     * @param principal the user that logged in
     * @return the role of the user
     */
    @Override
    public Role getRole(final OAuth2User principal) {
        return "APPROLE_Admin".equals(principal.getAuthorities()
                .toArray()[0].toString())
                ? Role.ADMIN : Role.USER;
    }
}
