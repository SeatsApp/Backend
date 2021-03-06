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
     * Disables a seat for reservations.
     * @param seatId the id of the to be disabled seat.
     */
    void changeAvailability(Long seatId);

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
     * Gets all the seat with their reservations
     * by the user who reserved them.
     *
     * @param email the email of the user to find
     *              the seats with the reservations
     * @return the list of the found seats
     */
    List<Seat> getAllByUser(String email);

    /**
     * Checks in on the reservation of the seat.
     *
     * @param seatId   the seatId from the seat where you check in.
     * @param username username of the person wanting to check in.
     */
    void checkInOnSeat(Long seatId, String username);

    /**
     * Update the seat with id by the object seat.
     *
     * @param seatId the seat id of the seat that will be updated
     * @param seat the seat with the data to update the given seat with id
     * @return the updated seat
     */
    Seat updateSeat(Long seatId, Seat seat);
}
