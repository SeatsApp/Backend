package com.seatapp.services;

import com.seatapp.domain.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

        assertTrue(reservation.checkIfDateIsBeforeNow(startTimeNew));
    }

    @Test
    void checkIfDateIsInValid() {
        Reservation reservation = new Reservation();

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR14, 0, 0);

        assertFalse(reservation.checkIfDateIsBeforeNow(startTimeNew));
    }
}
