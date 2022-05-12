package com.seatapp.repositories.usermanagement;

import com.seatapp.domain.usermanagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryImpl extends
        JpaRepository<User, Long>, UserRepository {
    /**
     * Finds the user by email.
     *
     * @param email the email to search the user
     * @return the optional user
     */
    @Override
    Optional<User> findByEmail(String email);

    /**
     * Check if the user exists.
     *
     * @param email the email to search the user
     * @return a boolean to check if the user exists
     */
    @Override
    Boolean existsByEmail(String email);
}
