package com.seatapp.repositories;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ReservationRepositoryTests {
    /**
     * represents the reservation.
     */
    @Autowired
    private ReservationRepositoryImpl reservationRepository;

    /**
     * Represent the hours that are used in a test.
     */
    private static final int HOURS3 = 3;


    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
    }

    @Test
    @Transactional
    void saveSeat() {
        // Arrange
        Reservation entity = new Reservation(
                1L,
                LocalDate.now().atTime(HOURS3, 0),
                LocalDate.now().atTime(HOURS3, 0),
                false,
                new User(),
                false);

        //Act
        Reservation reservation = reservationRepository.save(entity);

        //Assert
        assertEquals(entity.getStartDateTime(),
                reservation.getStartDateTime());
    }

    @Test
    @Transactional
    void findReservationById() {
        // Arrange
        Reservation entity = new Reservation(1L,
                LocalDate.now().atTime(HOURS3, 0),
                LocalDate.now().atTime(HOURS3, 0),
                false,
                new User(),
                false);
        entity = reservationRepository.save(entity);

        //Act
        Reservation reservation =
                reservationRepository.findById(entity.getId());

        //Assert
        assertEquals(entity.getStartDateTime(),
                reservation.getStartDateTime());
    }
}
