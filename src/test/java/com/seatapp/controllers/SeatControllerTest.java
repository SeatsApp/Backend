package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.SeatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SeatControllerTest {
    /**
     * Represents mockMvc.
     */
    @Autowired
    private MockMvc mockMvc;

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
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
        createSeatUrl = "/api/seat";
    }

    @Test
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
}