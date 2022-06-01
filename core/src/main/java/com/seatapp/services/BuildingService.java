package com.seatapp.services;

import com.seatapp.domain.Building;
import java.time.LocalDate;
import java.util.List;

public interface BuildingService {
    /**
     * Get all the buildings.
     *
     * @return a list of the found buildings
     */
    List<Building> getAll();

    /**
     * Gets the building by building id, floor id and
     * the reservations by date.
     *
     * @param buildingId the building id
     * @param floorId the floor id
     * @param date the date for filtering the reservations
     * @return the found building
     */
    Building getByIdAndFloorIdAndDate(long buildingId,
                                      long floorId,
                                      LocalDate date);

    /**
     * get building by building id and floor id.
     *
     * @param buildingId the building id
     * @param floorId the floor id
     * @return the found building
     */
    Building getByIdAndFloorId(long buildingId, long floorId);
}
