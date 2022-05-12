package com.seatapp.repositories;

import com.seatapp.domain.Seat;

import java.util.List;


public interface SeatRepository {
    /**
     * Saving the seat in the database.
     *
     * @param seat the seat you want to save
     * @return The updated seat due to
     * the changes from the database
     */
    Seat save(Seat seat);

    /**
     * Deleting the given seat from the database.
     *
     * @param id id of the seat that will be deleted
     */
    void deleteById(Long id);

    /**
     * Get all the seats from the database.
     *
     * @return all the seats from the database
     * in a list
     */
    List<Seat> findAll();

    /**
     * Tries to find the seat with the given id.
     *
     * @param seatId the id of the seat you want to find
     * @return the seat with the given id.
     */
    Seat findById(Long seatId);

    /**
     * Delete all the seats in the database.
     */
    void deleteAll();
}
