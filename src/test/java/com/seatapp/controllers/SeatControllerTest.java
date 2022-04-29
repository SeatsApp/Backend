package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class SeatControllerTest {
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR = 2022;
    /**
     * The month used in tests.
     */
    private static final int DATE_MONTH = 4;
    /**
     * The day used in tests.
     */
    private static final int DATE_DAY = 27;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR_13 = 13;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR_17 = 17;
    /**
     * An hour used in the tests.
     */
    private static final int DATE_HOUR_14 = 14;
    /**
     * Represents mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Represents the seat repository.
     */
    @Autowired
    private SeatRepository seatRepository;

    /**
     * Represents objectMapper for json conversions.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Represents api url for createSeat.
     */
    private String createSeatUrl;
    /**
     * Represents api url for /api/seats/.
     */
    private String apiSeatsUrl;

    /**
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
        createSeatUrl = "/api/seat";
        apiSeatsUrl = "/api/seats/";
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        SeatDto seatDto = new SeatDto("Test");

        mockMvc.perform(post(createSeatUrl)
                        .content(objectMapper.writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createSeatWithNoName() throws Exception {
        mockMvc.perform(post(createSeatUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithEmptyStringAsName() throws Exception {
        SeatDto seatDto = new SeatDto("");

        mockMvc.perform(post(createSeatUrl)
                        .content(objectMapper.writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithNameNull() throws Exception {
        SeatDto seatDto = new SeatDto(null);

        mockMvc.perform(post(createSeatUrl)
                        .content(objectMapper.writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithDtoBeingNull() throws Exception {
        mockMvc.perform(post(createSeatUrl)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteSeatWithValidId() throws Exception {
        Seat toBeDeletedSeat = seatRepository.save(new Seat("TestSeat"));

        mockMvc.perform(delete(apiSeatsUrl + toBeDeletedSeat.getId()))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Seat with id: " + toBeDeletedSeat.getId()
                                + " is successfully removed."));
    }

    @Test
    @Transactional
    void deleteSeatWithInValidId() throws Exception {
        mockMvc.perform(delete("/api/seats/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("No seat with this id."));
    }

    @Test
    @Transactional
    void getSeats() throws Exception {
        Seat seat1 = seatRepository.save(new Seat("Test1"));
        Seat seat2 = seatRepository.save(new Seat("Test2"));

        mockMvc.perform(get("/api/seats"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[{\"id\": " + seat1.getId()
                                + ", \"name\": \"Test1\"},"
                                + "{\"id\": " + seat2.getId()
                                + ", \"name\": \"Test2\"}]"));
    }

    @Test
    @Transactional
    void getSeatsWithEmptyDatabase() throws Exception {
        mockMvc.perform(get("/api/seats"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[]"));
    }

    @Test
    @Transactional
    void reserveSeatWithValidId() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        Seat toBeReservedSeat = seatRepository.save(new Seat("TestSeat"));

        mockMvc.perform(patch(apiSeatsUrl + toBeReservedSeat.getId()
                        + "/reserve")
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("You reserved "
                                + toBeReservedSeat.getName() + "."));
    }

    @Test
    @Transactional
    void reserveSeatWithNoValidId() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        mockMvc.perform(patch("/api/seats/1/reserve")
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("No seat with this id."));
    }

    @Test
    @Transactional
    void reserveSeatThatIsReservedCase1() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime, endTime);

        Seat toBeReservedSeat = new Seat(
                "TestSeat");

        toBeReservedSeat.getReservations()
                .add(new Reservation(startTime, endTime));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + "/reserve").content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Timeslot already booked."));
    }

    @Test
    @Transactional
    void reserveSeatThatIsReservedCase2() throws Exception {
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew);

        Seat toBeReservedSeat = new Seat(
                "Seat");

        toBeReservedSeat.getReservations()
                .add(new Reservation(startTimeExisting, endTimeExisting));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + "/reserve").content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Timeslot already booked."));
    }

    @Test
    @Transactional
    void reserveSeatThatIsReservedCase3() throws Exception {
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew);

        Seat toBeReservedSeat = new Seat(
                "Seat");

        toBeReservedSeat.getReservations()
                .add(new Reservation(startTimeExisting, endTimeExisting));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + "/reserve").content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Timeslot already booked."));
    }

}
