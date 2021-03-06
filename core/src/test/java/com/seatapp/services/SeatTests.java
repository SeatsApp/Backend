package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Role;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SeatTests {
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR_FUTURE = 2024;
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
    private static final int DATE_HOUR13 = 13;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR15 = 15;
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
                    "User1", Role.ADMIN);

    @Test
    void addReservationTestPassed() {
        Seat seat = new Seat("Test");
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR13, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        seat.getReservations().add(new Reservation(startTimeExisting,
                endTimeExisting, VALID_USER));

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR16, 0, 0);

        seat.addReservation(new Reservation(startTimeNew,
                endTimeNew, VALID_USER));

        assertEquals(2, seat.getReservations().size());
        assertEquals("2024-04-27", seat.getReservations()
                .get(1).getStartDateTime().toLocalDate().toString());
    }

    @Test
    void addReservationTestFailed() {
        Seat seat = new Seat("Test");
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR13, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR15, 0, 0);
        seat.getReservations().add(
                new Reservation(startTimeExisting, endTimeExisting,
                        VALID_USER));

        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR13, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_FUTURE,
                DATE_MONTH, DATE_DAY, DATE_HOUR16, 0, 0);
        Reservation newReservation = new Reservation(startTimeNew, endTimeNew,
                VALID_USER);

        assertThrows(IllegalArgumentException.class,
                () -> seat.addReservation(newReservation));

        assertEquals(1, seat.getReservations().size());
    }
}

