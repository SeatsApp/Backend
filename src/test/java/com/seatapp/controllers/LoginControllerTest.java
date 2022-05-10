package com.seatapp.controllers;

import com.seatapp.services.usermanagement.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class LoginControllerTest {
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
     * Represents the user service.
     */
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    void healthCheckWithFilter() throws Exception {
        // Arrange
        String jwt = userService.login("Thomas",
                "test@hotmail.com", "Thomas");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Act
        mockMvc
                .perform(get("/api/healthcheck")
                        .header("authorization", "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void healthCheckWithFilterAndInvalidToken() throws Exception {
        // Arrange
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        String jwt = "eyJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJUaG9tYXMgVmFuIERlIFdhbGxlIiwiZXhwIjoxNjUyMDc5ODg3"
                + "LCJpYXQiOjE2NTE5OTM0ODd9.BPwt8xEyEkBUapIQKYpJP"
                + "dDt80khWQzB7Nm7TsIJhbvjJ5msbRusifMHEzHkc4mDo8Ih0W9g";

        // Act
        mockMvc
                .perform(get("/api/healthcheck")
                        .header("authorization", "Bearer " + jwt))
                .andExpect(status().isBadRequest());
    }

    @Test
    void nHealthCheckWithFilterWithoutToken() throws Exception {
        // Arrange
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        // Act
        mockMvc
                .perform(get("/api/healthcheck"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void loginWeb() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain).build();

        mockMvc.perform(get("/api/login/web").with(
                        oauth2Login().oauth2User(new OAuth2User() {
                            @Override
                            public Map<String, Object> getAttributes() {
                                return new HashMap<>();
                            }

                            @Override
                            public Collection<? extends GrantedAuthority>
                            getAuthorities() {
                                return new ArrayList<>();
                            }

                            @Override
                            public String getName() {
                                return "Thomas";
                            }
                        })))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Transactional
    void loginExpo() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .addFilter(springSecurityFilterChain).build();

        mockMvc.perform(get("/api/login/expo").with(
                        oauth2Login().oauth2User(new OAuth2User() {
                            @Override
                            public Map<String, Object> getAttributes() {
                                return new HashMap<>();
                            }

                            @Override
                            public Collection<? extends GrantedAuthority>
                            getAuthorities() {
                                return new ArrayList<>();
                            }

                            @Override
                            public String getName() {
                                return "Thomas";
                            }
                        })))
                .andExpect(status().is3xxRedirection());
    }
}
