package com.seatapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import javax.transaction.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
     * This method sets up necessary items for the tests.
     */
    @BeforeEach
    void setup() {
        createSeatUrl = "/api/seat";
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

        mockMvc.perform(delete("/api/seats/" + toBeDeletedSeat.getId()))
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
}
