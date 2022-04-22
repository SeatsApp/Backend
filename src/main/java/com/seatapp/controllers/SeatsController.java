package com.seatapp.controllers;

import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is an api to create, read and delete seats.
 */
@RestController
@RequestMapping("/api/")
public class SeatsController {
    /**
     * Represents the service that is called.
     */
    private final SeatService seatService;

    /**
     * Creates the controller with a specified service.
     * @param seatService the seatService
     */
    @Autowired
    public SeatsController(final SeatService seatService) {
        this.seatService = seatService;
    }

    /**
     * Creates a seat.
     * @param seatDto is a Dto containing the name of the seat.
     * @return Returns a responseEntity with the HttpStatus and a message.
     */
    @PostMapping("/seat")
    public ResponseEntity<String> createSeat(
            @RequestBody final SeatDto seatDto) {
        Seat createdSeat = seatService.createSeat(seatDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Seat with name: \"" + createdSeat.getName()
                        + "\" is successfully created.");
    }
}