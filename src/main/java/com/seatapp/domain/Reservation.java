package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
public class Reservation {
    /**
     * Represents the reservations' id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Represents the start time of a reservation.
     */
    private LocalDateTime startTime;
    /**
     * Represents the end time of a reservation.
     */
    private LocalDateTime endTime;

    /**
     * Creates reservation with the details.
     * @param startTime start time of the reservation.
     * @param endTime end time of the reservation.
     */
    public Reservation(final LocalDateTime startTime,
                      final LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Checks if the new reservation is valid.
     * @param existingReservations reservations of the seat.
     */
    public void checkNewReservation(final List<Reservation>
                                            existingReservations) {
        LocalDateTime startTimeNew = this.getStartTime();
        LocalDateTime endTimeNew = this.getEndTime();
        for (Reservation seatRes : existingReservations) {
            LocalDateTime startTimeExisting = seatRes.getStartTime();
            LocalDateTime endTimeExisting = seatRes.getEndTime();

            if (startTimeNew.isEqual(startTimeExisting)
                    || startTimeNew.isAfter(startTimeExisting)
                    && startTimeNew.isBefore(endTimeExisting)
                    || startTimeNew.isBefore(startTimeExisting)
                    && endTimeNew.isAfter(startTimeExisting)) {
                throw new IllegalArgumentException("Timeslot already booked.");
            }
        }
    }
}
