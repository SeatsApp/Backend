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
     * Represents the x coordinates in centimeter
     * for generating the svg.
     */
    private int xCoordinates;

    /**
     * Represents the y coordinates in centimeter
     * for generating the svg.
     */
    private int yCoordinates;

    /**
     * Represents the width in centimeter
     * for generating the svg.
     */
    private int width;

    /**
     * Represents the height in centimeter
     * for generating the svg.
     */
    private int height;

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
     * Creates a seat with a specified name.
     *
     * @param name         The seats' name
     * @param xCoordinates the x coordinates in the svg
     * @param yCoordinates the y coordinates in the svg
     * @param width        the width in the svg
     * @param height       the height in the svg
     */
    public Seat(final String name,
                final int xCoordinates, final int yCoordinates,
                final int width, final int height) {
        this.name = name;
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
        this.width = width;
        this.height = height;
        this.reservations = new ArrayList<>();
        this.available = true;
    }

    /**
     * Adds a reservation to the list after checks.
     *
     * @param newReservation the new reservation.
     */
    public void addReservation(final Reservation newReservation) {
        List<Reservation> filteredReservations = new ArrayList<>(
                this.reservations.stream()
                        .filter(reservation -> !reservation.isCancelled())
                        .toList());
        if (newReservation.isValidNewReservation(filteredReservations)) {
            List<Reservation> newReservations =
                    new ArrayList<>(getReservations());
            newReservations.add(newReservation);
            this.setReservations(newReservations);
        } else {
            throw new IllegalArgumentException("The reservation is not valid");
        }
    }
}
