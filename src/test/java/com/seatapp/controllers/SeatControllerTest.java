package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.domain.usermanagement.User;
import com.seatapp.repositories.SeatRepository;
import com.seatapp.services.usermanagement.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
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
    private static final int DATE_YEAR = 2024;
    /**
     * The year used in the tests.
     */
    private static final int DATE_YEAR_PAST = 2022;
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
     * Username used in the tests.
     */
    private static final User VALID_USER =
            new User("User1",
                    "User@Test.be",
                    "User1");

    /**
     * jwt token used in the tests.
     */
    private String jwt;

    /**
     * Authentication token used in the tests.
     */
    private UsernamePasswordAuthenticationToken token;

    /**
     * Represents mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Represents the spring security filter chain.
     */
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

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
     * Represents api url for /api/seats/.
     */
    private String apiSeatsUrl;
    /**
     * Represents api url for /reserve.
     */
    private String reserveString;

    /**
     *  Represents userService.
     */
    @Autowired
    private UserService userService;

    /**
     * The application context.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String authorizationString;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String bearerString;

    /**
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
        apiSeatsUrl = "/api/seats/";
        reserveString = "/reserve";
        seatRepository.deleteAll();
        authorizationString = "authorization";
        bearerString = "Bearer ";
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain)
                .build();

        jwt = userService.login("User1",
                "User@Test.be", "PW");

        token = new UsernamePasswordAuthenticationToken(
                "User1", "PW");
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        SeatDto seatDto = new SeatDto(1L, "Test");

        mockMvc.perform(post(apiSeatsUrl)
                        .with(authentication(token))
                        .header(authorizationString,
                                bearerString + jwt)
                        .content(objectMapper
                                .writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createSeatWithNoName() throws Exception {
        mockMvc.perform(post(apiSeatsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithEmptyStringAsName() throws Exception {
        SeatDto seatDto = new SeatDto(1L, "");

        mockMvc.perform(post(apiSeatsUrl)
                        .content(objectMapper
                                .writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithNameNull() throws Exception {
        SeatDto seatDto = new SeatDto(1L, null);

        mockMvc.perform(post(apiSeatsUrl)
                        .content(objectMapper
                                .writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithDtoBeingNull() throws Exception {
        mockMvc.perform(post(apiSeatsUrl)
                        .content(objectMapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteSeatWithValidId() throws Exception {
        Seat toBeDeletedSeat = seatRepository.save(new Seat("TestSeat"));

        mockMvc.perform(delete(apiSeatsUrl
                        + toBeDeletedSeat.getId())
                        .with(authentication(token))
                .header(authorizationString, bearerString
                        + jwt))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void deleteSeatWithInValidId() throws Exception {
        mockMvc.perform(delete(apiSeatsUrl + "1").with(authentication(token))
                .header(authorizationString, bearerString
                        + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void getSeats() throws Exception {
        Seat seat1 = seatRepository.save(new Seat("Test1"));
        Seat seat2 = seatRepository.save(new Seat("Test2"));

        mockMvc.perform(get(apiSeatsUrl)
                        .with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt))
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
        mockMvc.perform(get(apiSeatsUrl)
                        .with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt))
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
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString + jwt)
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
                        .with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt)
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
                .add(new Reservation(startTime, endTime, VALID_USER));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
                .add(new Reservation(startTimeExisting, endTimeExisting,
                        VALID_USER));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
                .add(new Reservation(startTimeExisting, endTimeExisting,
                        VALID_USER));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void reserveSeatThatHasInvalidTimes() throws Exception {
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew);

        Seat toBeReservedSeat = new Seat(
                "Seat");

        toBeReservedSeat.getReservations()
                .add(new Reservation(startTimeExisting, endTimeExisting,
                        VALID_USER));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void reserveSeatThatHasDateInPast() throws Exception {
        LocalDateTime startTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTimeExisting = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew);

        Seat toBeReservedSeat = new Seat(
                "Seat");

        toBeReservedSeat.getReservations()
                .add(new Reservation(startTimeExisting, endTimeExisting,
                        VALID_USER));

        Seat savedSeat = seatRepository.save(toBeReservedSeat);

        mockMvc.perform(patch(apiSeatsUrl + savedSeat.getId()
                        + reserveString).with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void getSeatsWithReservationsByDate() throws Exception {
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        Seat seat1 = new Seat("Test1");
        seat1.addReservation(new Reservation(startTimeNew, endTimeNew,
                VALID_USER));
        Seat savedSeat = seatRepository.save(seat1);

        mockMvc.perform(get("/api/seats/reservations/date/2024-04-27")
                        .with(authentication(token))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[{\"id\": " + savedSeat.getId()
                                + ", \"name\": \"Test1\","
                                + "\"reservations\": [{ \"id\": "
                                + savedSeat.getReservations().get(0).getId()
                                + ", "
                                + "\"startTime\": \"2024-04-27T14:00:00\", "
                                + "\"endTime\": \"2024-04-27T17:00:00\", "
                                + "\"date\": \"2024-04-27\"}]}]"));
    }

    @Test
    @Transactional
    void checkInSeat() throws Exception {
        Seat seat1 = new Seat("Test1");
        seat1.getReservations().add(new Reservation(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                VALID_USER));
        Seat savedSeat = seatRepository.save(seat1);

        mockMvc.perform(patch("/api/seats/" + savedSeat.getId() + "/checkIn")
                        .with(authentication(token))
                        .header(authorizationString, bearerString + jwt))
                .andExpect(status().isOk());
    }
}
