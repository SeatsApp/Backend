package com.seatapp.dtos;

import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.controllers.dtos.SeatStatusDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Role;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SeatDtoTest {
    /**
     * Hours added in the tests.
     */
    private static final int HOURS_ADDED3 = 3;

    /**
     * Hours added in the tests.
     */
    private static final int HOURS_ADDED5 = 5;

    /**
     * Days added in the tests.
     */
    private static final int DAYS_ADDED1 = 1;

    /**
     * Hours in the tests.
     */
    private static final int HOURS0 = 0;

    /**
     * Minutes in the tests.
     */
    private static final int MINUTES0 = 0;

    @Test
    void seatDtoStatusAvailable() {
        // Arrange
        Seat seat = new Seat("Test");

        // Act
        SeatDto seatDto = SeatDto.build(seat,
                LocalDate.now().atTime(0, 0),
                LocalDate.now().plusDays(1).atTime(0, 0));

        // Assert
        assertEquals(SeatStatusDto.AVAILABLE, seatDto.getSeatStatus());
    }

    @Test
    void seatDtoStatusPartiallyBooked() {
        // Arrange
        Seat seat = new Seat("Test");

        seat.setReservations(List.of(new Reservation(LocalDateTime.now(),
                LocalDateTime.now().plusHours(HOURS_ADDED3), new User("test",
                "test", "test", Role.USER))));

        // Act
        SeatDto seatDto = SeatDto.build(seat,
                LocalDate.now().atTime(HOURS0, MINUTES0),
                LocalDate.now().plusDays(DAYS_ADDED1).atTime(HOURS0,
                        MINUTES0));

        // Assert
        assertEquals(SeatStatusDto.PARTIALLY_BOOKED, seatDto.getSeatStatus());
    }

    @Test
    void seatDtoStatusPartiallyBookedBetweenReservation() {
        // Arrange
        Seat seat = new Seat("Test");

        seat.setReservations(List.of(new Reservation(LocalDateTime.now(),
                LocalDateTime.now().plusHours(HOURS_ADDED5), new User("test",
                "test", "test", Role.USER))));

        // Act
        SeatDto seatDto = SeatDto.build(seat,
                LocalDate.now().atTime(HOURS_ADDED3, MINUTES0),
                LocalDate.now().plusDays(DAYS_ADDED1).atTime(HOURS0,
                        MINUTES0));

        // Assert
        assertEquals(SeatStatusDto.PARTIALLY_BOOKED, seatDto.getSeatStatus());
    }

    @Test
    void seatDtoStatusFullyBooked() {
        // Arrange
        Seat seat = new Seat("Test");

        seat.setReservations(List.of(new Reservation(LocalDate.now()
                        .atTime(HOURS0, MINUTES0),
                        LocalDate.now()
                                .atTime(HOURS0, MINUTES0)
                                .plusHours(HOURS_ADDED3),
                        new User("test",
                                "test", "test", Role.USER)),
                new Reservation(LocalDate.now()
                        .atTime(HOURS0, MINUTES0)
                        .plusHours(HOURS_ADDED3),
                        LocalDate.now()
                                .atTime(HOURS0, MINUTES0)
                                .plusHours(HOURS_ADDED3)
                                .plusHours(HOURS_ADDED3),
                        new User("test",
                                "test", "test", Role.USER))));

        // Act
        SeatDto seatDto = SeatDto.build(seat,
                LocalDate.now()
                        .atTime(HOURS0, MINUTES0),
                LocalDate.now()
                        .atTime(HOURS0, MINUTES0)
                        .plusHours(HOURS_ADDED3).plusHours(HOURS_ADDED3));

        // Assert
        assertEquals(SeatStatusDto.FULLY_BOOKED, seatDto.getSeatStatus());
    }

    @Test
    void seatDtoStatusUnavailable() {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setAvailable(false);

        // Act
        SeatDto seatDto = SeatDto.build(seat,
                LocalDate.now().atTime(HOURS0, MINUTES0),
                LocalDate.now().plusDays(DAYS_ADDED1).atTime(HOURS0,
                        MINUTES0));

        // Assert
        assertEquals(SeatStatusDto.UNAVAILABLE, seatDto.getSeatStatus());
    }
}
