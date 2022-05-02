package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.JwtUserDetails;
import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.usermanagement.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
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
    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load the user from the repository by username.
     *
     * @param username the username of the user that
     *                 you want to load
     * @return the found UserDetails
     */
    @SneakyThrows
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found with username: " + username));

        return JwtUserDetails.build(user);
    }
}
