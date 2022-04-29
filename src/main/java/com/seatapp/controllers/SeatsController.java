package com.seatapp.controllers;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

/**
 * This is an api to create, read and delete seats.
 */
@CrossOrigin
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

     /**
     * Deletes a seat with a certain id.
     * @param seatId the id of the to be deleted seat.
     * @return Returns a responseEntity with the HttpStatus and a message.
     */
    @DeleteMapping("/seats/{seatId}")
    public ResponseEntity<String> deleteSeat(
            @PathVariable final Long seatId) {
        Seat deletedSeat = seatService.delete(seatId);
        return ResponseEntity.ok("Seat with id: " + deletedSeat.getId()
                + " is successfully removed.");
    }

     /**
     * Takes all the seats from the database.
     * @return Returns a responseEntity with the HttpStatus and the found seats.
     */
    @GetMapping("/seats")
    public ResponseEntity<List<Seat>> getSeats() {
        List<Seat> foundSeats = seatService.getAll();
        return ResponseEntity.ok(foundSeats);
    }

    /**
     * Reserves an existing seat.
     * @param seatId the Id of the to be reserved seat.
     * @param reservationDto the reservation details.
     * @return Returns a response with the HttpStatus and a message.
     */
    @PatchMapping("/seats/{seatId}/reserve")
    public ResponseEntity<String> reserveSeat(@PathVariable
                                                  final Long seatId,
                                              @RequestBody final
                                              ReservationDto reservationDto) {
        Seat reservedSeat = seatService.reserve(seatId, reservationDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("You reserved " + reservedSeat.getName() + ".");
    }
}
