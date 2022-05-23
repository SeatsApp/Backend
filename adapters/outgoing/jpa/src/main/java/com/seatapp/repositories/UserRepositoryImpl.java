package com.seatapp.repositories;

import com.seatapp.domain.User;
import com.seatapp.entities.UserEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    /**
     * Represents the repository.
     */
    private final UserRepositoryJpa repository;

    /**
     * Creates the userRepoImpl.
     * @param repository the repository
     */
    @Autowired
    public UserRepositoryImpl(final UserRepositoryJpa repository) {
        this.repository = repository;
    }

    /**
     * Saves a user to the database.
     * @param user the user that will be saved
     *             in the database
     * @return the saved user
     */
    @Override
    public User save(final User user) {
        UserEntity userEntity = repository.save(UserEntity.build(user));

        return userEntity.toUser();
    }

    /**
     * Finds a user by its email.
     * @param email the email to search the user
     * @return the found user
     */
    @Override
    public User findByEmail(final String email) {
        Optional<UserEntity> entityOptional = repository.findByEmail(email);

        return entityOptional.map(UserEntity::toUser)
                .orElseThrow(() -> new EntityNotFoundException(
                        "This user doesn't exist."));
    }

    /**
     * Sees if a user exists with the email.
     * @param email the email to search the user
     * @return true if user found else false
     */
    @Override
    public Boolean existsByEmail(final String email) {
        return repository.existsByEmail(email);
    }
}
