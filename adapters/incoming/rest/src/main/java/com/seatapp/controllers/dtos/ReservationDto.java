package com.seatapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seatapp.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    /**
     * Represents the start time of a reservation.
     */
    @JsonFormat(pattern = "yyyy-M-d H:mm:ss")
    private LocalDateTime startDateTime;
    /**
     * Represents the end time of a reservation.
     */
    @JsonFormat(pattern = "yyyy-M-d H:mm:ss")
    private LocalDateTime endDateTime;

    /**
     * Represents if someone has already checked in.
     */
    private boolean checkedIn;

    /**
     * Represents the user of the reservation.
     */
    private UserDto user;

    /**
     * This method converts a reservation to a reservationDto.
     *
     * @param reservation the to be converted reservation
     * @return a reservation dto
     */
    public static ReservationDto build(final Reservation reservation) {
        return new ReservationDto(
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                reservation.isCheckedIn(), UserDto
                .build(reservation.getUser()));
    }
}
