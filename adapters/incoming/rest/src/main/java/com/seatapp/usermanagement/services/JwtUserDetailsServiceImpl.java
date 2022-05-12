package com.seatapp.usermanagement.services;

import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import com.seatapp.usermanagement.JwtUserDetails;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {
    /**
     * Represents the user repository.
     */
    private final UserRepository userRepository;

    /**
     * Create a JwtUserDetailsService.
     *
     * @param userRepository the user repository
     */
    @Autowired
    public JwtUserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load the user from the repository by email.
     *
     * @param email the email of the user that
     *              you want to load
     * @return the found UserDetails
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(final String email) {
        User user = userRepository.findByEmail(email);
        return JwtUserDetails.build(user);
    }
}
