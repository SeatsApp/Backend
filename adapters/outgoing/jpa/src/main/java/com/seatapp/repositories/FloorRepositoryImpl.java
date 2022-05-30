package com.seatapp.repositories;

import com.seatapp.domain.Floor;
import com.seatapp.entities.FloorEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FloorRepositoryImpl implements FloorRepository {
    /**
     * Represents the floor repository.
     */
    private final FloorRepositoryJpa repository;

    /**
     * Creates the floorRepositoryImpl.
     *
     * @param repository the repository.
     */
    @Autowired
    public FloorRepositoryImpl(
            final FloorRepositoryJpa repository) {
        this.repository = repository;
    }

    /**
     * get the floor by floor id.
     *
     * @param floorId the floor id
     * @return the found building
     */
    @Override
    public Floor findById(final long floorId) {
        FloorEntity floorEntity = repository.findById(floorId).orElseThrow(
                () -> new EntityNotFoundException(
                        "This floor does not exist."));

        return floorEntity.toFloor();
    }

    /**
     * Saving the floor in the database.
     *
     * @param floor the floor you want to save
     * @return The updated floor due to
     * the changes from the database
     */
    @Override
    public Floor save(final Floor floor) {
        FloorEntity floorEntity = FloorEntity.build(floor);
        floorEntity = repository.save(floorEntity);
        return floorEntity.toFloor();
    }

    /**
     * Deletes all the floors.
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
