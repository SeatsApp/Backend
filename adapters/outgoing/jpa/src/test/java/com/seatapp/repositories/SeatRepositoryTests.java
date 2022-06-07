package com.seatapp.repositories;

import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import com.seatapp.domain.Role;
import com.seatapp.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SeatRepositoryTests {
    /**
     * A seat object used in testing.
     */
    private static final Seat VALID_SEAT = new Seat(
            "TestSeat", new ArrayList<>(
            List.of(new Reservation(LocalDateTime.now(),
                    LocalDateTime.now().plusHours(2),
                    new User("test", "test",
                            "test", Role.ADMIN)))));

    /**
     * The seatRepositoryImplementation.
     */
    @Autowired
    private SeatRepositoryImpl seatRepository;

    /**
     * The floor repository.
     */
    @Autowired
    private FloorRepository floorRepository;

    @BeforeEach
    void setup() {
        seatRepository.deleteAll();
    }

    @Test
    @Transactional
    void saveSeat() {
        //Act
        Seat savedSeat = seatRepository.save(VALID_SEAT);

        //Assert
        assertEquals(VALID_SEAT.getName(), savedSeat.getName());
    }

    @Test
    @Transactional
    void deleteSeat() {
        //Arrange
        Floor floor = floorRepository.save(new Floor(1L, "", new ArrayList<>(),
                List.of(new Seat(
                        "TestSeat", new ArrayList<>()))));

        //Act
        seatRepository.deleteById(floor.getSeats().get(0).getId());

        //Assert
        assertThrows(EntityNotFoundException.class,
                () -> seatRepository.findById(floor.getSeats().get(0).getId()));
    }

    @Test
    @Transactional
    void deleteSeatWithInvalidId() {
        assertThrows(EntityNotFoundException.class,
                () -> seatRepository.deleteById(1L));
    }

    @Test
    @Transactional
    void findAllSeats() {
        //Arrange
        seatRepository.save(VALID_SEAT);
        seatRepository.save(new Seat("Test"));

        //Act
        List<Seat> foundSeats = seatRepository.findAll();

        //Assert
        assertEquals(2, foundSeats.size());
        assertEquals(VALID_SEAT.getName(), foundSeats.get(0).getName());
        assertEquals("Test", foundSeats.get(1).getName());
    }

    @Test
    @Transactional
    void findSeatById() {
        //Arrange
        Seat savedSeat = seatRepository.save(VALID_SEAT);

        //Act
        Seat foundSeat = seatRepository.findById(savedSeat.getId());

        //Assert
        assertNotNull(foundSeat);
        assertEquals(savedSeat.getId(), foundSeat.getId());
    }

    @Test
    @Transactional
    void findSeatByInvalidId() {
        assertThrows(EntityNotFoundException.class,
                () -> seatRepository.findById(1L));
    }

    @Test
    @Transactional
    void deleteAllSeats() {
        //Arrange
        seatRepository.save(VALID_SEAT);
        seatRepository.save(new Seat(2L, "Test",
                true, 0, 0,
                0, 0, new ArrayList<>()));

        //Act
        seatRepository.deleteAll();

        //Assert
        assertEquals(0, seatRepository.findAll().size());
    }
}
