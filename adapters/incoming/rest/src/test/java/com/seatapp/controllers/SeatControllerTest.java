package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.controllers.dtos.UserDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Role;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import com.seatapp.services.SeatService;
import com.seatapp.services.UserService;
import com.seatapp.usermanagement.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

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
            new User("User@Test.be",
                    "User1",
                    "User1", Role.ADMIN);

    /**
     * jwt token used in the tests.
     */
    private String jwt;

    /**
     * Authentication token used in the tests.
     */
    private UsernamePasswordAuthenticationToken authentication;

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
     * Represents the mocked user service.
     */
    @MockBean(name = "userServiceImpl")
    private UserService userService;

    /**
     * Represents the jwt service.
     */
    @Autowired
    private JwtService jwtService;

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
     * Represents the mocked seat service.
     */
    @MockBean(name = "seatServiceImpl")
    private SeatService seatService;

    /**
     * Represents the mocked user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
        apiSeatsUrl = "/api/seats/";
        reserveString = "/reserve";
        authorizationString = "authorization";
        bearerString = "Bearer ";
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain)
                .build();

        authentication = new UsernamePasswordAuthenticationToken(
                VALID_USER.getEmail(), "PW",
                List.of(
                        new SimpleGrantedAuthority(
                                "ADMIN")));

        jwt = jwtService.generateToken(authentication);

        User user = new User(VALID_USER.getEmail(), VALID_USER.getEmail(),
                VALID_USER.getEmail(), VALID_USER.getRole());

        when(userService.getByEmail(VALID_USER.getEmail())).thenReturn(user);
        when(userRepository.findByEmail(VALID_USER.getEmail()))
                .thenReturn(user);
    }

    @Test
    void createSeat() throws Exception {
        // Arrange
        SeatDto seatDto = new SeatDto(1L, "Test", null, null);

        when(seatService.createSeat(any(Seat.class)))
                .thenReturn(new Seat(1L, "Test", true,
                        new ArrayList<>()));

        // Act & Assert
        mockMvc.perform(post(apiSeatsUrl)
                        .with(authentication(authentication))
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
        SeatDto seatDto = new SeatDto(1L, "", null, null);

        mockMvc.perform(post(apiSeatsUrl)
                        .content(objectMapper
                                .writeValueAsString(seatDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSeatWithNameNull() throws Exception {
        SeatDto seatDto = new SeatDto(1L, null, null, null);

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
    void deleteSeatWithValidId() throws Exception {

        mockMvc.perform(delete(apiSeatsUrl
                        + "4")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSeatWithInValidId() throws Exception {
        doThrow(new IllegalArgumentException())
                .when(seatService).delete(1L);

        mockMvc.perform(delete(apiSeatsUrl + "1")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSeats() throws Exception {
        when(seatService.getAll()).thenReturn(List.of(
                new Seat(1L, "Test1",
                        true, new ArrayList<>()),
                new Seat(2L, "Test2",
                        true, new ArrayList<>())));

        mockMvc.perform(get(apiSeatsUrl)
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[{\"id\": 1"
                                + ", \"name\": \"Test1\"},"
                                + "{\"id\": 2"
                                + ", \"name\": \"Test2\"}]"));
    }

    @Test
    void getSeatsWithEmptyDatabase() throws Exception {
        mockMvc.perform(get(apiSeatsUrl)
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("[]"));
    }

    @Test
    void reserveSeatWithValidId() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime,
                endTime, false, new UserDto());

        Reservation reservation = new Reservation(
                reservationDto.getStartDateTime(),
                reservationDto.getEndDateTime(),
                new User());
        when(seatService.reserve(1L, reservation))
                .thenReturn(new Seat(1L, "Test",
                        true, new ArrayList<>()));

        mockMvc.perform(patch(apiSeatsUrl + 1L
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString + jwt)
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void reserveSeatWithNoValidId() throws Exception {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime,
                endTime, false, new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        // Act & Assert
        mockMvc.perform(patch("/api/seats/1/reserve")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt)
                        .content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveSeatThatIsReservedCase1() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto = new ReservationDto(startTime,
                endTime, false, new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch(apiSeatsUrl + 1
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveSeatThatIsReservedCase2() throws Exception {
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew,
                        endTimeNew, false, new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch(apiSeatsUrl + 1
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveSeatThatIsReservedCase3() throws Exception {
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew,
                        endTimeNew, false, new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch(apiSeatsUrl + 1
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveSeatThatHasInvalidTimes() throws Exception {
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_17, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR, DATE_MONTH,
                DATE_DAY, DATE_HOUR_13, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew, false,
                        new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch(apiSeatsUrl + 1
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveSeatThatHasDateInPast() throws Exception {
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR_PAST,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        ReservationDto reservationDto =
                new ReservationDto(startTimeNew, endTimeNew, false,
                        new UserDto());

        when(seatService.reserve(eq(1L), any(Reservation.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch(apiSeatsUrl + 1
                        + reserveString).with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt).content(objectMapper
                                .writeValueAsString(reservationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSeatsWithReservationsByDate() throws Exception {
        // Arrange
        LocalDateTime startTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_14, 0, 0);
        LocalDateTime endTimeNew = LocalDateTime.of(DATE_YEAR,
                DATE_MONTH, DATE_DAY, DATE_HOUR_17, 0, 0);

        Seat seat1 = new Seat("Test1");
        Reservation reservation = new Reservation(startTimeNew,
                endTimeNew, VALID_USER);
        reservation.setId(1L);
        seat1.addReservation(reservation);
        seat1.setId(1L);

        when(seatService.getAllWithReservationsByDate(
                LocalDate.of(DATE_YEAR,
                        DATE_MONTH, DATE_DAY)))
                .thenReturn(List.of(seat1));

        List<SeatDto> seatDtos = Stream.of(seat1)
                .map(seat -> SeatDto.build(seat,
                        startTimeNew.toLocalDate()
                                .atTime(0, 0),
                        startTimeNew.toLocalDate()
                                .plusDays(1).atTime(0, 0)))
                .toList();
        String json = objectMapper.writeValueAsString(seatDtos);

        // Act & Assert
        mockMvc.perform(get("/api/seats/reservations/date/2024-04-27")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(json));
    }

    @Test
    void checkInSeat() throws Exception {
        Seat seat1 = new Seat("Test1");
        seat1.getReservations().add(new Reservation(
                LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                VALID_USER));

        when(userService.existsByEmail(VALID_USER.getEmail()))
                .thenReturn(true);

        mockMvc.perform(patch("/api/seats/" + 1 + "/checkIn")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString + jwt))
                .andExpect(status().isOk());
    }
}
