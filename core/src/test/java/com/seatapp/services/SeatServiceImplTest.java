package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Role;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class SeatServiceImplTest {
    /**
     * Represents the seat repository.
     */
    @MockBean(name = "seatRepositoryImpl")
    private SeatRepository seatRepository;

    /**
     * Represents the seat service.
     */
    @Autowired
    private SeatService seatService;

    /**
     * Hours added in the tests.
     */
    private static final int HOURS_ADDED3 = 3;

    /**
     * Hours added in the tests.
     */
    private static final int HOURS_ADDED25 = 25;

    /**
     * Hours added in the tests.
     */
    private static final int HOURS_ADDED28 = 28;

    /**
     * Minutes removed in the tests.
     */
    private static final int MINUTES_REMOVED10 = 10;

    /**
     * Username used in the tests.
     */
    private static final User VALID_USER =
            new User("User1",
                    "User@Test.be",
                    "User1", Role.ADMIN);

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
                new Seat("Test"));

        //Assert
        Seat expectedSeat = new Seat(1L, "Test",
                true, null);
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
    void createSeatTestWithBlankName() {
        // Arrange
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.createSeat(new Seat("")));

        String expectedMessage = "The newSeat name is invalid.";
        String actualMessage = exception.getMessage();

        // Act & Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createSeatTestWithNameNull() {
        // Arrange
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.createSeat(new Seat(null)));

        String expectedMessage = "The newSeat name is invalid.";
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
                .thenReturn(null);

        // Act
        seatService.delete(toBeDeletedSeat.getId());

        // Assert
        assertNull(seatRepository.findById(toBeDeletedSeat.getId()));
    }

    @Test
    void deleteSeatWithInvalidId() {
        doThrow(new EntityNotFoundException(""))
                .when(seatRepository).deleteById(1L);

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

    @Test
    void getSeatsOnDate() {
        // Arrange
        Seat correctSeat = new Seat("correct");
        correctSeat.addReservation(new Reservation(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(HOURS_ADDED3), new User()));

        Seat badSeat = new Seat("bad");
        badSeat.addReservation(new Reservation(
                LocalDateTime.now().plusHours(HOURS_ADDED25),
                LocalDateTime.now().plusHours(HOURS_ADDED28), new User()));
        given(seatRepository.findAll()).willReturn(
                List.of(correctSeat, badSeat));

        // Act
        List<Seat> seats =
                seatService.getAllWithReservationsByDate(LocalDate.now());

        // Assert
        assertNotNull(seats);
        assertFalse(seats.get(0).getReservations().isEmpty());
        assertTrue(seats.get(1).getReservations().isEmpty());
    }

    @Test
    void reserveSeatTest() {
        // Arrange
        when(seatRepository.findById(1L))
                .thenReturn(new Seat("Test"));
        when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        // Act
        Seat seat = seatService.reserve(1L,
                new Reservation(LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(HOURS_ADDED3),
                        new User()));

        // Assert
        assertEquals("Test", seat.getName());
    }

    @Test
    void reserveSeatTestStartTimeAfterEndTime() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(new Seat("Test"));
        when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                seatService.reserve(1L,
                        new Reservation(LocalDateTime.now()
                                .plusHours(HOURS_ADDED3),
                                LocalDateTime.now().plusHours(1),
                                new User())));
    }

    @Test
    void reserveUnavailableSeatTest() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(new Seat(1L,
                "Test", false, new ArrayList<>()));
        when(seatRepository.save(Mockito.any(Seat.class)))
                .thenAnswer(i -> {
                    Seat seat = i.getArgument(0);
                    seat.setId(1L);
                    return seat;
                });

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                seatService.reserve(1L,
                        new Reservation(LocalDateTime.now().plusHours(1),
                                LocalDateTime.now().plusHours(HOURS_ADDED3),
                                new User())));
    }

    @Test
    void reserveSeatTestWithNull() {
        // Arrange
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.reserve(1L, null));

        String expectedMessage = "ReservationDto cannot be null";
        String actualMessage = exception.getMessage();

        // Act & Assert
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkInSeat() {
        //given
        Seat seat = new Seat("Seat1");
        seat.setId(1L);
        seat.getReservations().add(new Reservation(
                LocalDateTime.now().minusMinutes(MINUTES_REMOVED10),
                LocalDateTime.now().plusHours(1),
                VALID_USER));

        given(seatRepository.findById(
                seat.getId())).willReturn(seat);

        //act
        seatService.checkInOnSeat(seat.getId(), VALID_USER.getEmail());

        //assert
        assertTrue(seat.getReservations().get(0).isCheckedIn());
    }

    @Test
    void checkInSeatOnWrongTime() {
        //given
        Seat seat = new Seat("Seat1");
        seat.setId(1L);
        seat.getReservations().add(new Reservation(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), VALID_USER));

        when(seatRepository.findById(seat.getId()))
                .thenReturn(seat);

        String username = VALID_USER.getEmail();
        Long seatId = seat.getId();

        //assert
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seatService.checkInOnSeat(seatId,
                        username));

        String expectedMessage = "You can't check in before the start"
                + " time or after the end time.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAllByUser() {
        //given
        Seat seat1 = new Seat("Test");
        seat1.getReservations().add(new Reservation(1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                new User("test", "", "", Role.USER),
                false));
        seat1.getReservations().add(new Reservation(1L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                new User("someone else", "", "", Role.USER),
                false));
        seat1.setId(1L);

        given(seatRepository.findAll()).willReturn(List.of(seat1));

        //act
        List<Seat> seats = seatService.getAllByUser("test");

        //assert
        assertEquals(1L, seats.get(0).getReservations().get(0).getId());
        assertEquals(1, seats.get(0).getReservations().size());
    }
}
