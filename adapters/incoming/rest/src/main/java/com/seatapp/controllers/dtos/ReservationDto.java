package com.seatapp.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private LocalDateTime startTime;
    /**
     * Represents the end time of a reservation.
     */
    @JsonFormat(pattern = "yyyy-M-d H:mm:ss")
    private LocalDateTime endTime;
}
