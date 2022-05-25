package com.seatapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
@NoArgsConstructor
public class UserReservationDto {
    /**
     * Represents the id of the reservation.
     */
    private long id;

    /**
     * Represents the start time of a reservation.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;
    /**
     * Represents the end time of a reservation.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    /**
     * Represents the seat on which the reservation is made.
     */
    private String seatName;

    /**
     * Represents if someone has already checked in.
     */
    private boolean checkedIn;

    /**
     * This method converts a reservation to a reservationDto.
     *
     * @param reservation the to be converted reservation
     * @param seat the seat on which the reservation is placed
     * @return a reservation dto
     */
    public static UserReservationDto build(final Reservation reservation,
                                           final Seat seat) {
        return new UserReservationDto(
                reservation.getId(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                seat.getName(),
                reservation.isCheckedIn());
    }

    /**
     * This method converts a list of seats with reservations
     * to a userReservationDto.
     *
     * @param seats the list of seats with reservations
     * @return a list of userReservationDto
     */
    public static List<UserReservationDto> buildList(
            final List<Seat> seats) {
        List<UserReservationDto> userReservationDtos =
                new ArrayList<>();
        for (Seat seat : seats) {
            for (Reservation reservation : seat.getReservations()) {
                userReservationDtos.
                        add(UserReservationDto.build(reservation, seat));
            }
        }
        return userReservationDtos;
    }
}
