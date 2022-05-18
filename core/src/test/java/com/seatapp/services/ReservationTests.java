package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReservationTests {
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR_FUTURE = 2024;
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR_PAST = 2020;
    /**
     * The month used in tests.
     */
    private static final int DATE_MONTH = 4;
    /**
     * The day used in tests.
     */
    private static final int DATE_DAY = 27;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR1 = 13;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR15 = 15;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR14 = 14;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR16 = 16;
    /**
     * Username used in the tests.
     */
    private static final User VALID_USER =
            new User("User1",
                    "User@Test.be",
                    "User1");

    @Test
    void checkIfTimeslotFreeWithSameHours() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);

        assertFalse(reservation.checkIfTimeslotFree(startTimeNew,
                startTimeExisting, endTimeNew, endTimeExisting));

    }

    @Test
    void checkIfTimeslotFreeWithTimeBetweenExisting() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR16, 0, 0);

        assertFalse(reservation.checkIfTimeslotFree(startTimeNew,
                startTimeExisting, endTimeNew, endTimeExisting));
    }

    @Test
    void checkIfTimeslotFreeWithTimeBetweenExisting2() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR16, 0, 0);

        assertFalse(reservation.checkIfTimeslotFree(startTimeNew,
                startTimeExisting, endTimeNew, endTimeExisting));
    }

    @Test
    void checkIfTimeIsInValid() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);

        assertFalse(reservation.checkIfStartTimeIsBeforeEndTime(startTimeNew,
                endTimeNew));
    }

    @Test
    void checkIfTimeIsValid() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);

        assertTrue(reservation.checkIfStartTimeIsBeforeEndTime(startTimeNew,
                endTimeNew));
    }

    @Test
    void checkIfDateIsValid() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);

        assertTrue(reservation.checkIfDateIsAfterNow(startTimeNew));
    }

    @Test
    void checkIfDateIsInValid() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);

        assertFalse(reservation.checkIfDateIsAfterNow(startTimeNew));
    }

    @Test
    void checkInTest() {
        LocalDateTime startTimeNew = LocalDateTime.now();
        LocalDateTime endTimeNew = LocalDateTime.now().plusHours(1);

        Reservation reservation = new Reservation(startTimeNew, endTimeNew,
                VALID_USER);

        reservation.checkIn(VALID_USER.getEmail());

        assertTrue(reservation.isCheckedIn());
    }

    @Test
    void checkInAlreadyCheckedInReservation() {
        LocalDateTime startTimeNew = LocalDateTime.now();
        LocalDateTime endTimeNew = LocalDateTime.now().plusHours(1);

        Reservation reservation = new Reservation(startTimeNew, endTimeNew,
                VALID_USER);
        String username = VALID_USER.getEmail();
        reservation.checkIn(username);


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> reservation.checkIn(username));

        String expectedMessage = "This reservation is already checked in.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkInWrongUsername() {
        LocalDateTime startTimeNew = LocalDateTime.now();
        LocalDateTime endTimeNew = LocalDateTime.now().plusHours(1);

        Reservation reservation = new Reservation(startTimeNew, endTimeNew,
                VALID_USER);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> reservation.checkIn("NotValidUsername"));

        String expectedMessage =
                "You didn't reserve this seat for this timeslot.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
