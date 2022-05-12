package com.seatapp.usermanagement.services;

import org.springframework.security.core.Authentication;

public interface LoginService {
    /**
     * Login to the application.
     *
     * @param email    the email of the user
     * @param fullName the full name of the user
     * @param password the unencoded password
     * @return the authentication of the user that logged in.
     */
    Authentication login(String email, String fullName,
                                String password);
}
