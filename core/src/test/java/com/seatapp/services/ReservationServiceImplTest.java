package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import com.seatapp.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReservationServiceImplTest {
    /**
     * Represents the reservation repository.
     */
    @MockBean(name = "reservationRepositoryImpl")
    private ReservationRepository reservationRepository;

    /**
     * Represents the reservation service.
     */
    @Autowired
    private ReservationServiceImpl reservationService;

    @Test
    void createSeatTest() {
        // Arrange
        when(reservationRepository.findById(1L))
                .thenReturn(new Reservation())
                .thenReturn(new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        true));
        when(reservationRepository.save(Mockito.any(Reservation.class)))
                .thenAnswer(i -> {
                    Reservation reservation = i.getArgument(0);
                    reservation.setId(1L);
                    return reservation;
                });

        //Act
        reservationService.cancelReservation(1L, "test@cronos.be");

        //Assert
        Reservation reservation = reservationRepository.findById(1L);
        assertTrue(reservation.isCancelled());
    }
}
