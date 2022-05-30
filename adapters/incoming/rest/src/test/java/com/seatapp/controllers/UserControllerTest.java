package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.UserDto;
import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {
    /**
     * Represents the mocked user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * Represents mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Represents the mocked seat service.
     */
    @MockBean(name = "userServiceImpl")
    private UserService userService;

    /**
     * Represents the spring security filter chain.
     */
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    /**
     * Authentication token used in the tests.
     */
    private UsernamePasswordAuthenticationToken authentication;

    /**
     * The application context.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Username used in the tests.
     */
    private static final User VALID_USER =
            new User("User@Test.be",
                    "User1",
                    "User1", Role.ADMIN);

    /**
     * Represents the jwt service.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * jwt token used in the tests.
     */
    private String jwt;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String authorizationString;

    /**
     * Variable to prevent repetitive strings in code.
     */
    private String bearerString;

    /**
     * Represents objectMapper for json conversions.
     */
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
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
    void getUsers() throws Exception {
        when(userService.getAll()).thenReturn(List.of(
                new User("Jari@test.be", "Test1", "Test", Role.ADMIN),
                new User("Thomas@test.be", "Test2",
                        "Test", Role.ADMIN)));

        mockMvc.perform(get("/api/users")
                        .with(authentication(authentication))
                        .header(authorizationString, bearerString
                                + jwt))
                .andExpect(status().isOk());
    }

    @Test
    void changeUserRole() throws Exception {
        UserDto userDto = new UserDto("Test", "Test", Role.ADMIN);

        mockMvc.perform(patch("/api/users/role")
                .with(authentication(authentication))
                .header(authorizationString, bearerString
                        + jwt).content(objectMapper
                        .writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
