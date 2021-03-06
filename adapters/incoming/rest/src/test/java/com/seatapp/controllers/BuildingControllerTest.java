package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.BuildingDto;
import com.seatapp.domain.Seat;
import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.User;
import com.seatapp.domain.Role;
import com.seatapp.domain.Point;
import com.seatapp.repositories.UserRepository;
import com.seatapp.services.BuildingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class BuildingControllerTest {
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
     * Represents objectMapper for json conversions.
     */
    @Autowired
    private ObjectMapper objectMapper;

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
     * Loading in the data for the floor id.
     */
    private static final long FLOOR_ID3 = 3;

    /**
     * Represents the mocked user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * Represents the mocked building service.
     */
    @MockBean(name = "buildingServiceImpl")
    private BuildingService buildingService;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String authorizationString;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String bearerString;

    /**
     * Loading in the data for the building name.
     */
    private static final String BUILDING_NAME1 = "Building 1";

    /**
     * Loading in the data for the floor name.
     */
    private static final String FLOOR_NAME1 = "Floor 1";

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
    void getBuildingByIdAndFloorIdTest() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Building building = new Building(1L, BUILDING_NAME1,
                List.of(floor));

        when(buildingService.getByIdAndFloorId(
                1L, 2L))
                .thenReturn(building);

        // Act & Assert
        mockMvc.perform(get("/api/buildings/1/floors/2")
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBuildingByIdAndFloorIdAndDateTest() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Building building = new Building(1L, BUILDING_NAME1,
                List.of(floor));

        when(buildingService.getByIdAndFloorIdAndDate(
                1L, 2L, LocalDate.now()))
                .thenReturn(building);

        // Act & Assert
        mockMvc.perform(get(
                        "/api/buildings/1/floors/2?date="
                                + LocalDate.now())
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBuildingByIdTest() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Building building = new Building(1L, BUILDING_NAME1,
                List.of(floor));

        when(buildingService.getById(1L))
                .thenReturn(building);

        // Act & Assert
        mockMvc.perform(get(
                        "/api/buildings/1")
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void postBuilding() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor1 = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Floor floor2 = new Floor(FLOOR_ID3, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Building buildingContent = new Building(1L, BUILDING_NAME1,
                List.of(floor1, floor2));

        BuildingDto buildingDto = BuildingDto.build(buildingContent);

        when(buildingService.createBuilding(any(Building.class)))
                .thenReturn(buildingContent);

        // Act & Assert
        mockMvc.perform(post(
                        "/api/buildings")
                        .with(authentication(authentication))
                        .content(objectMapper
                                .writeValueAsString(buildingDto))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void patchBuilding() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor1 = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Floor floor2 = new Floor(FLOOR_ID3, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        Building building = new Building(1L, BUILDING_NAME1,
                List.of(floor1));

        Building buildingContent = new Building(1L, BUILDING_NAME1,
                List.of(floor1, floor2));

        BuildingDto buildingDto = BuildingDto.build(buildingContent);

        when(buildingService.getById(1L))
                .thenReturn(building);

        // Act & Assert
        mockMvc.perform(patch(
                        "/api/buildings/1")
                        .with(authentication(authentication))
                        .content(objectMapper
                                .writeValueAsString(buildingDto))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBuildings() throws Exception {
        // Arrange
        Seat seat = new Seat("Test");
        seat.setReservations(List.of(
                new Reservation(1L,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        new User(),
                        false)));

        Floor floor = new Floor(2L, FLOOR_NAME1,
                List.of(new Point(0, 0)), List.of(seat));

        Building building = new Building(1L, BUILDING_NAME1,
                List.of(floor));

        when(buildingService.getAll())
                .thenReturn(List.of(building));

        // Act & Assert
        mockMvc.perform(get(
                        "/api/buildings")
                        .with(authentication(authentication))
                        .header(authorizationString,
                                bearerString + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
