package com.seatapp.controllers.dtos;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

    /**
     * Represents the seat id.
     */
    private Long id;
    /**
     * Represents the seats' name.
     */
    private String name;

    /**
     * Represents the seat status.
     */
    private SeatStatusDto seatStatus;

    /**
     * Represents the reservation of the seat.
     */
    private List<ReservationDto> reservations;

    /**
     * Represents if the seat is available for reservations.
     */
    private boolean available;

    /**
     * This method converts a seat to a seatDto without
     * giving the seats a status.
     *
     * @param seat the to be converted seat
     * @return a seat dto
     */
    public static SeatDto build(final Seat seat) {

        return new SeatDto(seat.getId(),
                seat.getName(),
                null,
                seat.getReservations().stream()
                        .map(ReservationDto::build)
                        .toList(), seat.isAvailable());
    }

    /**
     * This method converts a seat to a seatDto.
     *
     * @param seat          the to be converted seat
     * @param startDateTime the startDateTime to see from
     *                      when the status of the seat is available
     * @param endDateTime   the endDateTime to see until when
     *                      the status of the seats is available
     * @return a seat dto
     */
    public static SeatDto build(final Seat seat,
                                final LocalDateTime startDateTime,
                                final LocalDateTime endDateTime) {
        SeatStatusDto status = findSeatStatus(seat,
                startDateTime, endDateTime);

        return new SeatDto(seat.getId(),
                seat.getName(),
                status,
                seat.getReservations().stream()
                        .map(ReservationDto::build)
                        .toList(), seat.isAvailable());
    }

    /**
     * Gives back the status of the seat.
     *
     * @param seat          the seat for which the status will be determined
     * @param startDateTime the startDateTime to see from
     *                      when the status of the seat is available
     * @param endDateTime   the endDateTime to see until when
     *                      the status of the seats is available
     * @return this return the status of the seat in a SeatStatusDto
     */
    private static SeatStatusDto findSeatStatus(
            final Seat seat,
            final LocalDateTime startDateTime,
            final LocalDateTime endDateTime) {
        if (!seat.isAvailable()) {
            return SeatStatusDto.UNAVAILABLE;
        }

        if (seat.getReservations().isEmpty()) {
            return SeatStatusDto.AVAILABLE;
        }

        List<Reservation> reservations =
                sortReservationsOnStartDateTime(seat.getReservations());
        LocalDateTime newStartDateTime = startDateTime;
        for (Reservation reservation : reservations) {
            if (startTimeBetweenReservation(newStartDateTime, reservation)) {
                newStartDateTime = reservation.getEndDateTime();
            } else {
                return SeatStatusDto.PARTIALLY_BOOKED;
            }

            if (newStartDateTime.isEqual(endDateTime)) {
                return SeatStatusDto.FULLY_BOOKED;
            }
        }

        return SeatStatusDto.PARTIALLY_BOOKED;
    }

    /**
     * Sorts the reservation on the start date time.
     *
     * @param reservations the reservations which
     *                     should be sorted
     * @return the sorted reservations
     */
    private static List<Reservation> sortReservationsOnStartDateTime(
            final List<Reservation> reservations) {
        List<Reservation> newReservations =
                new ArrayList<>(reservations);
        newReservations
                .sort((o1, o2) -> o1.getStartDateTime()
                        .isBefore(o2.getStartDateTime()) ? 1 : 0);
        return newReservations;
    }

    /**
     * Checks if the start time is at the start or between
     * a reservation.
     *
     * @param startDateTime the start time to check up on
     * @param reservation   the reservation closest to
     *                      the start time
     * @return a boolean if the start time is at the start
     * or between the reservation
     */
    private static boolean startTimeBetweenReservation(
            final LocalDateTime startDateTime,
            final Reservation reservation) {
        return startDateTime.isEqual(reservation.getStartDateTime())
                || startDateTime.isAfter(reservation.getStartDateTime())
                && startDateTime.isBefore(reservation.getEndDateTime());
    }
}
