package com.seatapp.controllers.dtos;

public enum SeatStatusDto {
    /**
     * The seat can always be reserved.
     */
    AVAILABLE,
    /**
     * The seat can partially be reserved due to
     * other reservations.
     */
    PARTIALLY_BOOKED,
    /**
     * The seat can not be reserved for this time
     * due to other reservations.
     */
    FULLY_BOOKED,
    /**
     * The seat can not be reserved.
     */
    UNAVAILABLE
}
