package com.seatapp.entities;

import com.seatapp.domain.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

@Entity
@Table(name = "Seat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatEntity {
    /**
     * Represents the seats' id.
     */
    @Id
    @GeneratedValue
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;

    /**
     * This method converts a seat to a seatEntity.
     *
     * @param seat the to be converted seat
     * @return a seat entity
     */
    public static SeatEntity build(final Seat seat) {
        return new SeatEntity(seat.getId(), seat.getName(),
                seat.isAvailable(),
                seat.getXCoordinates(),
                seat.getYCoordinates(),
                seat.getWidth(),
                seat.getHeight(),
                seat.getReservations().stream()
                        .map(ReservationEntity::build)
                        .toList());
    }

    /**
     * This method converts a seatEntity to a seat.
     *
     * @return a seat
     */
    public Seat toSeat() {
        return new Seat(this.getId(), this.getName(),
                this.isAvailable(),
                this.getXCoordinates(),
                this.getYCoordinates(),
                this.getWidth(),
                this.getHeight(),
                getReservations().stream()
                        .map(ReservationEntity::toReservation)
                        .toList());
    }
}
