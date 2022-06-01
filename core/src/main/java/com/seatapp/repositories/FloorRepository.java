package com.seatapp.repositories;

import com.seatapp.domain.Floor;

public interface FloorRepository {
    /**
     * get the floor by floor id.
     *
     * @param floorId the floor id
     * @return the found building
     */
    Floor findById(long floorId);

    /**
     * Saving the floor in the database.
     *
     * @param floor the floor you want to save
     * @return The updated floor due to
     * the changes from the database
     */
    Floor save(Floor floor);

    /**
     * Deletes all the floors.
     */
    void deleteAll();
}
