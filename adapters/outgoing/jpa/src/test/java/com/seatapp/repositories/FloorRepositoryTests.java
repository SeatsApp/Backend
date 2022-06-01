package com.seatapp.repositories;

import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FloorRepositoryTests {
    /**
     * Floor used in tests.
     */
    private Floor validFloor;

    /**
     * The floorRepositoryImplementation.
     */
    @Autowired
    private FloorRepositoryImpl floorRepository;

    @BeforeEach
    void setup() {
        floorRepository.deleteAll();

        Seat seat = new Seat("Test");
        seat.setReservations(List.of());

        validFloor = new Floor(2L, "Floor 1",
                List.of(), List.of(seat));
    }

    @Test
    @Transactional
    void findFloorById() {
        //Arrange
        Floor savedFloor = floorRepository.save(validFloor);

        //Act
        Floor foundFloor = floorRepository.findById(savedFloor.getId());

        //Assert
        assertNotNull(foundFloor);
        assertEquals(savedFloor.getId(), foundFloor.getId());
    }
}
