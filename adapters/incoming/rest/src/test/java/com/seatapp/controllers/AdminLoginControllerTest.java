package com.seatapp.controllers;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import com.seatapp.usermanagement.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class AdminLoginControllerTest {
    /**
     * Represents mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Represents the web application context.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Represents the spring security filter chain.
     */
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    /**
     * Represents the jwt service.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Represents the mocked user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * Represents the mocked authentication manager.
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void adminHealthCheck() throws Exception {
        String jwt = jwtService.generateToken(
                new UsernamePasswordAuthenticationToken(
                        "test", "test", List.of(
                        new SimpleGrantedAuthority(
                                "ADMIN"))));
        when(userRepository.findByEmail("test"))
                .thenReturn(new User("test", "test", "testje", Role.ADMIN));

        // Arrange
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Act
        mockMvc
                .perform(get("/api/admin/healthcheck")
                        .header("authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    void adminHealthCheckWithUser() throws Exception {
        String jwt = jwtService.generateToken(
                new UsernamePasswordAuthenticationToken(
                        "test", "test", List.of(
                        new SimpleGrantedAuthority(
                                "USER"))));
        when(userRepository.findByEmail("test"))
                .thenReturn(new User("test", "test",
                        "testName", Role.USER));

        // Arrange
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Act
        mockMvc
                .perform(get("/api/admin/healthcheck")
                        .header("authorization", "Bearer " + jwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginAdminWeb() throws Exception {
        // Arrange
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain).build();

        String email = "thomas.vandewalle@cronos.be";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, email);
        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);

        // Act & Assert
        mockMvc.perform(get("/api/admin/login").with(
                        oauth2Login().oauth2User(new OAuth2User() {
                            @Override
                            public Map<String, Object> getAttributes() {
                                Map<String, Object> map = new HashMap<>();
                                map.put("preferred_username",
                                        "thomas.vandewalle@cronos.be");
                                return map;
                            }

                            @Override
                            public Collection<? extends GrantedAuthority>
                            getAuthorities() {
                                return List.of(
                                        new SimpleGrantedAuthority(
                                                "APPROLE_Admin"));
                            }

                            @Override
                            public String getName() {
                                return "Thomas";
                            }
                        })))
                .andExpect(status().is3xxRedirection());
    }
}
