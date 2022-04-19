package com.seatapp.services;

import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
}
