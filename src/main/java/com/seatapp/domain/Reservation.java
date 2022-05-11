package com.seatapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
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
     * Represents the date of a reservation.
     */
    private LocalDate date;

    /**
     * Creates reservation with the details.
     *
     * @param startTime start time of the reservation.
     * @param endTime   end time of the reservation.
     */
    public Reservation(final LocalDateTime startTime,
                       final LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = startTime.toLocalDate();
    }

    /**
     * Checks if the new reservation is valid.
     *
     * @param existingReservations reservations of the seat.
     * @return boolean that indicates if the reservation is valid.
     */
    public boolean isValidNewReservation(final List<Reservation>
                                                 existingReservations) {
        LocalDateTime startTimeNew = this.getStartTime();
        LocalDateTime endTimeNew = this.getEndTime();
        if (!checkIfStartTimeIsBeforeEndTime(startTimeNew, endTimeNew)) {
            return false;
        }
        if (!checkIfDateIsAfterNow(startTimeNew)) {
            return false;
        }
        return checkReservationsIfTimeslotFree(existingReservations,
                startTimeNew, endTimeNew);
    }

    private boolean checkReservationsIfTimeslotFree(
            final List<Reservation> existingReservations,
            final LocalDateTime startTimeNew,
            final LocalDateTime endTimeNew) {

        for (Reservation seatRes : existingReservations) {
            LocalDateTime startTimeExisting = seatRes.getStartTime();
            LocalDateTime endTimeExisting = seatRes.getEndTime();

            if (!checkIfTimeslotFree(startTimeNew, startTimeExisting,
                    endTimeNew, endTimeExisting)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the chosen timeslot is valid.
     *
     * @param startTimeNew      the chosen timeslots start time.
     * @param startTimeExisting the existing reservations start time.
     * @param endTimeNew        the chosen timeslots end time.
     * @param endTimeExisting   the existing reservations end time.
     * @return true if timeslot is free else false.
     */
    public boolean checkIfTimeslotFree(final LocalDateTime startTimeNew,
                                       final LocalDateTime startTimeExisting,
                                       final LocalDateTime endTimeNew,
                                       final LocalDateTime endTimeExisting) {
        if (startTimeNew.isEqual(startTimeExisting)) {
            return false;
        }
        if (startTimeNew.isAfter(startTimeExisting)
                && startTimeNew.isBefore(endTimeExisting)) {
            return false;
        }

        return !(startTimeNew.isBefore(startTimeExisting)
                && endTimeNew.isAfter(startTimeExisting));
    }

    /**
     * Checks if the end time isn't before the start time.
     *
     * @param startTimeNew start time of the timeslot.
     * @param endTimeNew   end time of the timeslot
     * @return true if timeslot is valid else false.
     */
    public boolean checkIfStartTimeIsBeforeEndTime(final LocalDateTime
                                                           startTimeNew,
                                                   final LocalDateTime
                                                           endTimeNew) {
        return startTimeNew.isBefore(endTimeNew);
    }

    /**
     * Checks if the start time isn't before the current date and time.
     *
     * @param startTimeNew start time of the timeslot.
     * @return true if date is valid else false.
     */
    public boolean checkIfDateIsAfterNow(final LocalDateTime startTimeNew) {
        return startTimeNew.isAfter(LocalDateTime.now());
    }
}
