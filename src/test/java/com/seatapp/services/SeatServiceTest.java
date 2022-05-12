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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
        // Arrange
        when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        //Act
        Seat savedSeat = seatService.createSeat(
                new SeatDto(1L, "Test", false));

        //Assert
        Seat expectedSeat = new Seat(1L, "Test",
                null);
        assertEquals(expectedSeat.getId(), savedSeat.getId());
        assertEquals(expectedSeat.getName(), savedSeat.getName());
    }

    @Test
    void createSeatTestWithNull() {
        // Arrange
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.createSeat(null));

        String expectedMessage = "SeatDto cannot be null";
        String actualMessage = exception.getMessage();

        // Act & Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteSeatWithValidId() {
        // Arrange
        Seat toBeDeletedSeat = new Seat("TestSeat");
        toBeDeletedSeat.setId(1L);

        when(seatRepository.findById(toBeDeletedSeat.getId()))
                .thenReturn(java.util.Optional.of(toBeDeletedSeat))
                .thenReturn(java.util.Optional.empty());

        // Act
        seatService.delete(toBeDeletedSeat.getId());

        // Assert
        assertNull(seatRepository.findById(toBeDeletedSeat.getId())
                .orElse(null));
    }

    @Test
    void deleteSeatWithInvalidId() {
        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> seatService.delete(1L));
    }

    @Test
    void getSeatsWillReturnSeats() {
        // Arrange
        Seat seat1 = new Seat("Test");
        seat1.setId(1L);

        Seat seat2 = new Seat("Test2");
        seat2.setId(2L);

        given(seatRepository.findAll()).willReturn(List.of(seat1, seat2));

        // Act
        List<Seat> seats = seatService.getAll();

        // Assert
        assertFalse(seats.isEmpty());
        assertEquals(2, seats.size());
    }

    @Test
    void getSeatsWillReturnEmptyListWhileNoSeatsInDatabase() {
        // Arrange
        given(seatRepository.findAll()).willReturn(List.of());

        // Act
        List<Seat> seats = seatService.getAll();

        // Assert
        assertNotNull(seats);
        assertTrue(seats.isEmpty());
    }
}
