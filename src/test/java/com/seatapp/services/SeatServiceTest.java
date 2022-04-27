package com.seatapp.services;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class SeatServiceTest {
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR = 2022;
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
    private static final int DATE_HOUR2 = 15;

    /**
     * Represents the seat repository.
     */
    @MockBean
    private SeatRepository seatRepository;

    /**
     * Represents the seat service.
     */
    @Autowired
    private SeatService seatService;

    @Test
    void createSeatTest() {
        //Given
        Mockito.when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        //Act
        Seat savedSeat = seatService.createSeat(new SeatDto("Test"));

        //Assert
        Seat expectedSeat = new Seat(1L, "Test", null);
        assertEquals(expectedSeat.getId(), savedSeat.getId());
        assertEquals(expectedSeat.getName(), savedSeat.getName());
    }

    @Test
    void createSeatTestWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.createSeat(null));

        String expectedMessage = "SeatDto cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteSeatWithValidId() {
        Seat toBeDeletedSeat = new Seat("TestSeat");
        toBeDeletedSeat.setId(1L);

        //Given
        Mockito.when(seatRepository.findById(toBeDeletedSeat.getId()))
                .thenReturn(java.util.Optional.of(toBeDeletedSeat));

        //Act
        Seat deletedSeat = seatService.delete(toBeDeletedSeat.getId());

        //Assert
        assertEquals(toBeDeletedSeat.getId(), deletedSeat.getId());
        assertEquals(toBeDeletedSeat.getName(), deletedSeat.getName());
    }

    @Test
    void deleteSeatWithNoValidId() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> {
                    seatService.delete(1L);
                });

        String expectedMessage = "No seat with this id.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getSeatsWillReturnSeats() {
        //given
        Seat seat1 = new Seat("Test");
        seat1.setId(1L);

        Seat seat2 = new Seat("Test2");
        seat2.setId(2L);

        given(seatRepository.findAll()).willReturn(List.of(seat1, seat2));

        //act
        List<Seat> seats = seatService.getAll();

        //assert
        assertFalse(seats.isEmpty());
        assertEquals(2, seats.size());
    }

    @Test
    void getSeatsWillReturnEmptyListWhileNoSeatsInDatabase() {
        //given
        given(seatRepository.findAll()).willReturn(List.of());

        //act
        List<Seat> seats = seatService.getAll();

        //assert
        assertNotNull(seats);
        assertTrue(seats.isEmpty());
    }

    @Test
    void reserveSeatWithValidId() {
        //Given
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR2, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        Seat toBeReservedSeat = new Seat("ReservedSeat");
        toBeReservedSeat.setId(1L);

        Mockito.when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        Mockito.when(seatRepository.findById(toBeReservedSeat.getId()))
                .thenReturn(java.util.Optional.of(toBeReservedSeat));

        //Act
        Seat reservedSeat = seatService
                .reserve(toBeReservedSeat.getId(), reservationDto);

        //Assert
        assertEquals(toBeReservedSeat.getId(), reservedSeat.getId());
        assertEquals(toBeReservedSeat.getName(), reservedSeat.getName());
        assertEquals(toBeReservedSeat.getReservations().get(0).getId(),
                reservedSeat.getReservations().get(0).getId());
    }

    @Test
    void reserveSeatWithNoValidId() {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR2, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> {
                    seatService.reserve(1L, reservationDto);
                });

        String expectedMessage = "No seat with this id.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reserveSeatWithThatIsAlreadyReserved() {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR2, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        Seat toBeReservedSeat = new Seat(1L, "ReservedSeat",
                new ArrayList<>());
        toBeReservedSeat.getReservations()
                .add(new Reservation(startTime, endTime));

        Mockito.when(seatRepository.findById(toBeReservedSeat.getId()))
                .thenReturn(java.util.Optional.of(toBeReservedSeat));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    seatService.reserve(1L, reservationDto);
                });

        String expectedMessage = "Timeslot already booked.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reserveSeatTestWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.reserve(1L, null));

        String expectedMessage = "ReservationDto cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
