package com.seatapp.services;


import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;

public interface FloorService {
    /**
     * Adds the seat to the floor.
     *
     * @param floor the floor on which the seat will be added
     * @param seat the seat that will be added to the floor
     * @return the floor with the added seat
     */
    Floor addSeat(Floor floor, Seat seat);

    /**
     * Get the floor by the id.
     *
     * @param floorId the floor id
     * @return the found floor
     */
    Floor findById(long floorId);
}
