package com.seatapp.controllers;

import com.seatapp.domain.Reservation;
import com.seatapp.domain.Role;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.repositories.UserRepository;
import com.seatapp.services.ReservationService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationControllerTest {
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
     * Authentication token used in the tests.
     */
    private UsernamePasswordAuthenticationToken authentication;

    /**
     * jwt token used in the tests.
     */
    private String jwt;

    /**
     * Username used in the tests.
     */
    private static final User VALID_USER =
            new User("User@Test.be",
                    "User1",
                    "User1", Role.ADMIN);

    /**
     * Represents the mocked user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * Represents the mocked reservation service.
     */
    @MockBean(name = "reservationService")
    private ReservationService reservationService;

    /**
     * Represents the mocked seat service.
     */
    @MockBean(name = "seatServiceImpl")
    private SeatService seatService;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String authorizationString;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String bearerString;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain)
                .build();

        authentication = new UsernamePasswordAuthenticationToken(
                VALID_USER.getEmail(), "PW",
                List.of(new SimpleGrantedAuthority(
                        "ADMIN")));

        jwt = jwtService.generateToken(authentication);

        authorizationString = "authorization";
        bearerString = "Bearer ";

        User user = new User(VALID_USER.getEmail(), VALID_USER.getEmail(),
                VALID_USER.getEmail(), VALID_USER.getRole());

        when(userService.getByEmail(VALID_USER.getEmail())).thenReturn(user);
        when(userRepository.findByEmail(VALID_USER.getEmail()))
                .thenReturn(user);
    }

    @Test
    void cancelReservation() throws Exception {
        // Arrange
        doThrow(new EntityNotFoundException(""))
                .when(reservationService).cancelReservation(1L, "");

        // Act & Assert
        mockMvc.perform(patch("/api/reservations/1/cancel")
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getReservationsOfTheUser() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        when(seatService
                .getAllByUser(VALID_USER.getEmail()))
                .thenReturn(List.of(seat));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/users")
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
