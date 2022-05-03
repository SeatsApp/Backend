package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seat {
    /**
     * Represents the seats' id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    /**
     * Represents the seats' name.
     */
    @NotBlank
    private String name;
    /**
     * Represents the reservations a seat.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    /**
     * Creates a seat with a specified name.
     * @param name The seats' name
     */
    public Seat(final String name) {
        this.name = name;
        this.reservations = new ArrayList<>();
    }
    /**
     * Adds a reservation to the list after checks.
     * @param newReservation the new reservation.
     */
    public void addReservation(final Reservation newReservation) {
        if (newReservation.isValidNewReservation(this.reservations)) {
            reservations.add(newReservation);
        }
    }
}
