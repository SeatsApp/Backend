package com.seatapp.usermanagement.services;

import org.springframework.security.core.Authentication;

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
     * Validate the JWT token.
     *
     * @param token the JWT token
     * @return if the JWT token is validated
     */
    boolean validateJwtToken(String token);
}
