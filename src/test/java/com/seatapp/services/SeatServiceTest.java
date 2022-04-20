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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Seat expectedSeat = new Seat(1L, "Test");
        assertEquals(expectedSeat.getId(), savedSeat.getId());
        assertEquals(expectedSeat.getName(), savedSeat.getName());
    }

    @Test
    void createSeatTestWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
            seatService.createSeat(null);
        });

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
}
