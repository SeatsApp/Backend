package com.seatapp.services;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.domain.usermanagement.User;

import java.time.LocalDate;
import java.util.List;

public interface SeatService {
    /**
     * Saves a Seat to the database.
     * @param seatDto The seatDto containing the name.
     * @return the Saved Seat.
     */
    Seat createSeat(SeatDto seatDto);

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
     * @param seatId         is the id of the to be reserved seat.
     * @param reservationDto is the reservation details.
     * @param user           the user wanting to make a reservation.
     * @return the reserved seat.
     */
    Seat reserve(Long seatId,
                 ReservationDto reservationDto,
                 User user);

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
