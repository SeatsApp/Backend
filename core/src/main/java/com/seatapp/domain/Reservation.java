package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
    /**
     * Represents the reservations' id.
     */
    private Long id;
    /**
     * Represents the start date time of a reservation.
     */
    private LocalDateTime startDateTime;
    /**
     * Represents the end date time of a reservation.
     */
    private LocalDateTime endDateTime;
    /**
     * Represents if reservation is checked in.
     */
    private boolean checkedIn;
    /**
     * Represents the user who placed the reservation.
     */
    private User user;

    /**
     * Represents when a reservation is cancelled.
     */
    private boolean cancelled;

    /**
     * Creates reservation with the details.
     *
     * @param startDateTime start time of the reservation.
     * @param endDateTime   end time of the reservation.
     * @param user          the user who made the reservation.
     */
    public Reservation(final LocalDateTime startDateTime,
                       final LocalDateTime endDateTime,
                       final User user) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.user = user;
        this.checkedIn = false;
        this.cancelled = false;
    }

    /**
     * Checks if the new reservation is valid.
     *
     * @param existingReservations reservations of the seat.
     * @return boolean that indicates if the reservation is valid.
     */
    public boolean isValidNewReservation(final List<Reservation>
                                                 existingReservations) {
        LocalDateTime startTimeNew = this.getStartDateTime();
        LocalDateTime endTimeNew = this.getEndDateTime();
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
            LocalDateTime startTimeExisting = seatRes.getStartDateTime();
            LocalDateTime endTimeExisting = seatRes.getEndDateTime();

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

    /**
     * Changes the checked in status of a reservation.
     *
     * @param username username of the person
     *                 wanting to check in.
     */
    public void checkIn(final String username) {
        if (isCheckedIn()) {
            throw new IllegalArgumentException(
                    "This reservation is already checked in.");
        }
        if (!username.equals(user.getEmail())) {
            throw new IllegalArgumentException(
                    "You didn't reserve this seat for this timeslot.");
        }
        this.checkedIn = true;
    }
}
