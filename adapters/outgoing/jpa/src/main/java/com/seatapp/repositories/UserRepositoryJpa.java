package com.seatapp.repositories;

import com.seatapp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryJpa extends
        JpaRepository<UserEntity, Long> {
    /**
     * Finds the user by email.
     *
     * @param email the email to search the user
     * @return the optional user
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Check if the user exists.
     *
     * @param email the email to search the user
     * @return a boolean to check if the user exists
     */
    Boolean existsByEmail(String email);
}
