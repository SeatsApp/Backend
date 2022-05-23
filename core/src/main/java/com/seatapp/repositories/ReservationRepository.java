package com.seatapp.repositories;

import com.seatapp.domain.Reservation;

public interface ReservationRepository {
    /**
     * Saving the reservation in the database.
     *
     * @param reservation the reservation you want to save
     * @return The updated reservation due to
     * the changes from the database
     */
    Reservation save(Reservation reservation);

    /**
     * Tries to find the reservation with the given id.
     *
     * @param reservationId the id of the reservation
     *                      you want to find
     * @return the reservation with the given id.
     */
    Reservation findById(Long reservationId);

    /**
     * Deletes all the reservations.
     */
    void deleteAll();
}
