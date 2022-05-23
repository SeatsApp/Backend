package com.seatapp.usermanagement.services;

import com.seatapp.domain.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtService {
    /**
     * Generates a JWT token.
     *
     * @param authentication the authentication on which
     *                       the JWT will be gennerated
     * @return a JWT token
     */
    String generateToken(Authentication authentication);

    /**
     * Get email by JWT token.
     *
     * @param token the JWT token
     * @return the email from the JWT token
     */
    String getEmailFromJwtToken(String token);

    /**
     * Get role by JWT token.
     *
     * @param token the JWT token
     * @return the email from the JWT token
     */
    Role getRoleFromJwtToken(String token);

    /**
     * Validate the JWT token.
     *
     * @param token the JWT token
     * @return if the JWT token is validated
     */
    boolean validateJwtToken(String token);

    /**
     * retrieves the JWT token out of the http request.
     *
     * @param request the request from which the JWT token
     *                will be retrieved.
     * @return the parsed JWT token
     */
    String parseJwt(HttpServletRequest request);
}
