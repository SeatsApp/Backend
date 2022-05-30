package com.seatapp.repositories;

import com.seatapp.domain.Building;
import java.util.List;

public interface BuildingRepository {
    /**
     * Saving the building in the database.
     *
     * @param building the building you want to save
     * @return The updated building due to
     * the changes from the database
     */
    Building save(Building building);


    /**
     * Get all the buildings from the database.
     *
     * @return all the buildings from the database
     * in a list
     */
    List<Building> findAll();

    /**
     * Tries to find the building with the given id.
     *
     * @param buildingId the id of the building you want to find
     * @return the building with the given id.
     */
    Building findById(Long buildingId);

    /**
     * Deletes all the buildings.
     */
    void deleteAll();
}
