package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {
    /**
     * Saves a Seat to the database.
     *
     * @param seat The seatDto containing the name.
     * @return the Saved Seat.
     */
    Seat createSeat(Seat seat);

    /**
     * Deletes the seat with the specified id.
     *
     * @param seatId the id of the to be deleted seat.
     */
    void delete(Long seatId);

    /**
     * Gets all the seats from database.
     *
     * @return a list of seats
     */
    List<Seat> getAll();

    /**
     * Reserves the seat with the specified id.
     *
     * @param seatId      is the id of the to be reserved seat.
     * @param reservation is the reservation details.
     * @return the reserved seat.
     */
    Seat reserve(Long seatId,
                 Reservation reservation);

    /**
     * Gets all the seats with their reservations from the given date.
     *
     * @param date is the date of the wanted reservations.
     * @return the list of seats.
     */
    List<Seat> getAllWithReservationsByDate(LocalDate date);

    /**
     * Checks in on the reservation of the seat.
     *
     * @param seatId   the seatId from the seat where you check in.
     * @param username username of the person wanting to check in.
     */
    void checkInOnSeat(Long seatId, String username);
}
