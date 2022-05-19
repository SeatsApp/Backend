package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    /**
     * Represents the seats' id.
     */
    private Long id;
    /**
     * Represents the seats' name.
     */
    private String name;

    /**
     * Represents if the seat is available for reservations.
     */
    private boolean available;

    /**
     * Represents the reservations a seat.
     */
    private List<Reservation> reservations;

    /**
     * Creates a seat with a specified name.
     *
     * @param name The seats' name
     */
    public Seat(final String name) {
        this.name = name;
        this.reservations = new ArrayList<>();
        this.available = true;
    }

    /**
     * Creates a seat with a specified name.
     *
     * @param name         The seats' name
     * @param reservations the seats' reservations
     */
    public Seat(final String name, final List<Reservation> reservations) {
        this.name = name;
        this.reservations = reservations;
        this.available = true;
    }

    /**
     * Adds a reservation to the list after checks.
     *
     * @param newReservation the new reservation.
     */
    public void addReservation(final Reservation newReservation) {
        if (newReservation.isValidNewReservation(this.reservations)) {
            reservations.add(newReservation);
        } else {
            throw new IllegalArgumentException("The reservation is not valid");
        }
    }
}
