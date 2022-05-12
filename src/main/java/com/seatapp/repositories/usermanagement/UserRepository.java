package com.seatapp.repositories.usermanagement;

import com.seatapp.domain.usermanagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds the user by email.
     *
     * @param email the email to search the user
     * @return the optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if the user exists.
     *
     * @param email the email to search the user
     * @return a boolean to check if the user exists
     */
    Boolean existsByEmail(String email);
}
