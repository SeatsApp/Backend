package com.seatapp.usermanagement.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface JwtUserDetailsService extends UserDetailsService {
    /**
     * Load the user from the repository by email.
     *
     * @param email the email of the user that
     *              you want to load
     * @return the found UserDetails
     */
    @Override
    UserDetails loadUserByUsername(String email);
}
