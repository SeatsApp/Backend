package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {
    /**
     * Represents the seat repository.
     */
    private final SeatRepository seatRepository;

    /**
     * Time before the start of a
     * reservation where you can check in.
     */
    @Value("${minutes.before.reservation}")
    private int minutesBeforeReservation;

    /**
     * Creates a service with the specified repository.
     *
     * @param seatRepository The seat repository.
     */
    @Autowired
    public SeatServiceImpl(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Saves a newSeat to the database.
     *
     * @param newSeat The new seat containing the name.
     * @return The saved new seat.
     */
    @Override
    public Seat createSeat(final Seat newSeat) {
        if (newSeat == null) {
            throw new IllegalArgumentException("SeatDto cannot be null");
        }
        if (newSeat.getName() == null || newSeat.getName().isBlank()) {
            throw new IllegalArgumentException("The newSeat name is invalid.");
        }
        return seatRepository.save(newSeat);
    }

    /**
     * Deletes the seat with the specified id.
     *
     * @param seatId the id of the to be deleted seat.
     */
    @Override
    public void delete(final Long seatId) {
        seatRepository.deleteById(seatId);
    }

    /**
     * Gets all the seats from database.
     *
     * @return a list of seats
     */
    @Override
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    /**
     * Gets a seat from database with the given id.
     *
     * @param seatId the id of the seat.
     * @return a seat
     */
    private Seat getSeatById(final Long seatId) {
        return seatRepository.findById(seatId);
    }

    /**
     * Reserves the seat with the specified id.
     *
     * @param seatId         is the id of the to be reserved seat.
     * @param newReservation is the newReservation details.
     * @return the reserved seat.
     */
    @Override
    public Seat reserve(final Long seatId,
                        final Reservation newReservation) {
        if (newReservation == null) {
            throw new IllegalArgumentException("ReservationDto cannot be null");
        }

        Seat seat = getSeatById(seatId);
        if (!seat.isAvailable()) {
            throw new IllegalArgumentException(
                    "You can't reserve when the seat is unavailable");
        }

        seat.addReservation(newReservation);
        seatRepository.save(seat);
        return seat;
    }

    /**
     * Gets all the seats with their reservations from the given date.
     *
     * @param date is the date of the wanted reservations.
     * @return the list of seats.
     */
    @Override
    public List<Seat> getAllWithReservationsByDate(final LocalDate date) {
        List<Seat> foundSeats = getAll();
        for (Seat s : foundSeats) {
            s.setReservations(s.getReservations().stream()
                    .filter(reservation -> reservation.getStartDateTime()
                            .toLocalDate().equals(date))
                    .filter(reservation -> !reservation.isCancelled())
                    .toList());
        }
        return foundSeats;
    }

    /**
     * Gets all the seat with their reservations
     * by the user who reserved them.
     *
     * @param email the email of the user to find
     *              the seats with the reservations
     * @return the list of the found seats
     */
    @Override
    public List<Seat> getAllByUser(final String email) {
        List<Seat> foundSeats = getAll();
        for (Seat s : foundSeats) {
            s.setReservations(s.getReservations().stream()
                    .filter(reservation -> reservation.getUser().getEmail()
                            .equals(email))
                    .filter(reservation ->
                            LocalDate.now()
                                    .atTime(0, 0)
                                    .minusHours(1)
                                    .isBefore(reservation.getStartDateTime()))
                    .filter(reservation -> !reservation.isCancelled())
                    .toList());
        }
        return foundSeats;
    }

    /**
     * Checks in on the reservation of the seat.
     *
     * @param seatId   the seatId from the seat where you check in.
     * @param username username of the person wanting to check in.
     */
    @Override
    public void checkInOnSeat(final Long seatId, final String username) {
        Seat seat = getSeatById(seatId);

        Reservation reservation = seat.getReservations().stream()
                .filter(res -> {
                    LocalDateTime startCheckInTime = res.getStartDateTime()
                            .minusMinutes(minutesBeforeReservation);
                    return LocalDateTime.now().isAfter(startCheckInTime);
                })
                .filter(res -> LocalDateTime.now()
                        .isBefore(res.getEndDateTime())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "You can't check in before the start"
                                + " time or after the end time."));

        reservation.checkIn(username);
        seatRepository.save(seat);
    }
}
