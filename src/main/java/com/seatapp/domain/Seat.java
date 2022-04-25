package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

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
     * Represents the reservation status of a seat.
     */
    private boolean isReserved;

    /**
     * Creates a seat with a specified name.
     * @param name The seats' name
     */
    public Seat(final String name) {
        this.name = name;
        this.isReserved = false;
    }
}
