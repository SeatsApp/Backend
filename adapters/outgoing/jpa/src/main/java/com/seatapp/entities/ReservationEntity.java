package com.seatapp.entities;

import com.seatapp.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationEntity {
    /**
     * Represents the reservations' id.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Represents the start time of a reservation.
     */
    private LocalDateTime startDateTime;

    /**
     * Represents the end time of a reservation.
     */
    private LocalDateTime endDateTime;

    /**
     * Represents if reservation is checked in.
     */
    private boolean checkedIn;

    /**
     * Represents when a reservation is cancelled.
     */
    private boolean cancelled;

    /**
     * Represents the user who placed the reservation.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    private UserEntity userEntity;

    /**
     * Creates reservation with the details.
     *
     * @param id         id of the reservation.
     * @param startDateTime  start time of the reservation.
     * @param endDateTime    end time of the reservation.
     * @param userEntity the user who made the reservation.
     * @param checkedIn  if the reservation is checkedIn or not
     * @param cancelled  if the reservation is cancelled or not
     */
    public ReservationEntity(final Long id,
                             final LocalDateTime startDateTime,
                             final LocalDateTime endDateTime,
                             final UserEntity userEntity,
                             final boolean checkedIn,
                             final boolean cancelled) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.checkedIn = checkedIn;
        this.userEntity = userEntity;
        this.cancelled = cancelled;
    }

    /**
     * This method converts a reservation to a reservationEntity.
     *
     * @param reservation the to be converted reservation
     * @return a reservation entity
     */
    public static ReservationEntity build(final Reservation reservation) {
        return new ReservationEntity(reservation.getId(),
                reservation.getStartDateTime(),
                reservation.getEndDateTime(),
                UserEntity.build(reservation.getUser()),
                reservation.isCheckedIn(),
                reservation.isCancelled());
    }

    /**
     * This method converts a reservationEntity to a reservation.
     *
     * @return a reservation
     */
    public Reservation toReservation() {
        return new Reservation(getId(), getStartDateTime(), getEndDateTime(),
                isCheckedIn(), userEntity.toUser(), isCancelled());
    }
}
