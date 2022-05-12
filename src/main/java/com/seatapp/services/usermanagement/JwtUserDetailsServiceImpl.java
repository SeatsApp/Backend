package com.seatapp.services.usermanagement;

import com.seatapp.domain.usermanagement.JwtUserDetails;
import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.usermanagement.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailService {
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User Not Found with email: " + email));

        return JwtUserDetails.build(user);
    }
}
