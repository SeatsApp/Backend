package com.seatapp.services;

import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import javax.persistence.EntityNotFoundException;
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
        Seat expectedSeat = new Seat(1L, "Test", false);
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
        Seat reservedSeat = seatService.reserve(toBeReservedSeat.getId());

        //Assert
        assertEquals(toBeReservedSeat.getId(), reservedSeat.getId());
        assertEquals(toBeReservedSeat.getName(), reservedSeat.getName());
    }

    @Test
    void reserveSeatWithNoValidId() {
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> {
                    seatService.reserve(1L);
                });

        String expectedMessage = "No seat with this id.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void reserveSeatWithThatIsAlreadyReserved() {
        Seat toBeReservedSeat = new Seat(1L, "ReservedSeat", true);

        Mockito.when(seatRepository.findById(toBeReservedSeat.getId()))
                .thenReturn(java.util.Optional.of(toBeReservedSeat));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    seatService.reserve(1L);
                });

        String expectedMessage = "Seat is already reserved.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
