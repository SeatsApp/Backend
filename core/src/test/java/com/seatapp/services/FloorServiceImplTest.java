package com.seatapp.services;

import com.seatapp.domain.Seat;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import com.seatapp.repositories.FloorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FloorServiceImplTest {
    /**
     * Represents the floor repository.
     */
    @MockBean(name = "floorRepositoryImpl")
    private FloorRepository floorRepository;

    /**
     * Represents the floor service.
     */
    @Autowired
    private FloorServiceImpl floorService;

    @Test
    void findFloorByIdTest() {
        // Arrange
        when(floorRepository.findById(2L))
                .thenReturn(new Floor(2L, "Floor 1",
                        new ArrayList<>(), new ArrayList<>()));

        //Act
        Floor floor = floorService.findById(2L);

        //Assert
        assertEquals("Floor 1", floor.getName());
    }

    @Test
    void addSeatTest() {
        // Arrange
        Reservation reservation = new Reservation(
                LocalDate.now().atTime(0, 0),
                LocalDate.now().plusDays(1).atTime(0, 0),
                new User());

        Seat seat = new Seat("A1");
        seat.setReservations(List.of(reservation));

        Floor floor = new Floor(2L, "Floor 1",
                new ArrayList<>(), new ArrayList<>());

        when(floorRepository.save(Mockito.any(Floor.class)))
                .thenAnswer(i -> {
                    Floor savedFloor = i.getArgument(0);
                    savedFloor.setId(1L);
                    return savedFloor;
                });

        //Act
        floor = floorService.addSeat(floor, seat);

        //Assert
        assertEquals(1, floor.getSeats().size());
    }
}
