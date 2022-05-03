package com.seatapp.services;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {
    /**
     * Represents the seat repository.
     */
    private final SeatRepository seatRepository;

    /**
     * Creates a service with the specified repository.
     *
     * @param seatRepository The seat repository.
     */
    @Autowired
    public SeatService(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Saves a seat to the database.
     *
     * @param seatDto The seatDto containing the name.
     * @return The saved seat.
     */
    public Seat createSeat(final SeatDto seatDto) {
        if (seatDto == null) {
            throw new IllegalArgumentException("SeatDto cannot be null");
        }
        if (seatDto.getName() == null || seatDto.getName().isBlank()) {
            throw new IllegalArgumentException("The seat name is invalid.");
        }
        return seatRepository.save(new Seat(seatDto.getName()));
    }

    /**
     * Deletes the seat with the specified id.
     *
     * @param seatId the id of the to be deleted seat.
     * @return the deleted seat.
     */
    public Seat delete(final Long seatId) {
        Seat seat = getSeatById(seatId);
        seatRepository.delete(seat);
        return seat;
    }

    /**
     * Gets all the seats from database.
     * @return a list of seats
     */
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    /**
     * Gets a seat from database with the given id.
     * @param seatId the id of the seat.
     * @return a seat
     */
    private Seat getSeatById(final Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No seat with this id."));
    }

    /**
     * Reserves the seat with the specified id.
     * @param seatId         is the id of the to be reserved seat.
     * @param reservationDto is the reservation details.
     * @return the reserved seat.
     */
    public Seat reserve(final Long seatId,
                        final ReservationDto reservationDto) {
        if (reservationDto == null) {
            throw new IllegalArgumentException("ReservationDto cannot be null");
        }
        Seat seat = getSeatById(seatId);
        Reservation newReservation = new Reservation(
                reservationDto.getStartTime(), reservationDto.getEndTime());
        seat.addReservation(newReservation);
        seatRepository.save(seat);
        return seat;
    }

    /**
     * Gets all the seats with their reservations from the given date.
     * @param date is the date of the wanted reservations.
     * @return the list of seats.
     */
    public List<Seat> getAllWithReservationsByDate(final LocalDate date) {
        List<Seat> foundSeats = getAll();
        for (Seat s :foundSeats) {
            s.setReservations(s.getReservations().stream()
                    .filter(reservation -> reservation.getDate().equals(date))
                    .collect(Collectors.toList()));
        }
        return foundSeats;
    }
}
