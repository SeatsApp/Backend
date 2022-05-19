package com.seatapp.controllers;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.services.SeatService;
import com.seatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

/**
 * This is an api to create, read and delete seats.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/seats")
public class SeatsController {
    /**
     * Represents the seat service that is called.
     */
    private final SeatService seatService;

    /**
     * Represents the user service that is called.
     */
    private final UserService userService;

    /**
     * Creates the controller with a specified service.
     *
     * @param seatService the seatService
     * @param userService the userService
     */
    @Autowired
    public SeatsController(final SeatService seatService,
                           final UserService userService) {
        this.seatService = seatService;
        this.userService = userService;
    }

    /**
     * Creates a seat.
     *
     * @param seatDto is a Dto containing the name of the seat.
     * @return Returns a responseEntity with the HttpStatus and a message.
     */
    @PostMapping
    public ResponseEntity<String> createSeat(
            @RequestBody final SeatDto seatDto) {
        Seat seat = new Seat(seatDto.getName());
        Seat createdSeat = seatService.createSeat(seat);
        return ResponseEntity.created(URI.create("/api/seats/"
                + createdSeat.getId())).build();
    }

    /**
     * Deletes a seat with a certain id.
     *
     * @param seatId the id of the to be deleted seat.
     * @return Returns a responseEntity with the HttpStatus and a message.
     */
    @DeleteMapping("{seatId}")
    public ResponseEntity<String> deleteSeat(
            @PathVariable final Long seatId) {
        seatService.delete(seatId);
        return ResponseEntity.ok().build();
    }

    /**
     * Takes all the seats from the database.
     *
     * @return Returns a responseEntity with the HttpStatus and the found seats.
     */
    @GetMapping
    public ResponseEntity<List<Seat>> getSeats() {
        List<Seat> foundSeats = seatService.getAll();
        return ResponseEntity.ok(foundSeats);
    }

    /**
     * Takes all the seats from the database with the
     * reservations from the given date.
     *
     * @param date the date where you want reservations from.
     * @return Returns a responseEntity with the HttpStatus and the found seats.
     */
    @GetMapping("reservations/date/{date}")
    public ResponseEntity<List<SeatDto>> getSeatsWithReservationsByDate(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PathVariable final LocalDate date) {
        List<Seat> foundSeats = seatService
                .getAllWithReservationsByDate(date);

        List<SeatDto> seatDtos = foundSeats.stream()
                .map(seat -> SeatDto.build(seat,
                        date.atTime(0, 0),
                        date.plusDays(1).atTime(0, 0)))
                .toList();
        return ResponseEntity.ok(seatDtos);
    }

    /**
     * Reserves an existing seat.
     *
     * @param seatId         the Id of the to be reserved seat.
     * @param reservationDto the reservation details.
     * @param token          the authentication information.
     * @return Returns a response with the HttpStatus and a message.
     */
    @PatchMapping("{seatId}/reserve")
    public ResponseEntity<String>
    reserveSeat(@PathVariable final Long seatId,
                @RequestBody final
                ReservationDto reservationDto,
                final
                UsernamePasswordAuthenticationToken
                        token) {
        User user = userService.getByEmail(token.getName());
        Reservation reservation = new Reservation(
                reservationDto.getStartDateTime(),
                reservationDto.getEndDateTime(),
                user);
        seatService.reserve(seatId, reservation);
        return ResponseEntity.ok().build();
    }

    /**
     * Checks in on an existing seat.
     *
     * @param seatId the Id of the to be checked in seat.
     * @param token  the authentication information.
     * @return Returns a response with the HttpStatus and a message.
     */
    @PatchMapping("{seatId}/checkIn")
    public ResponseEntity<String> checkIn(@PathVariable final Long seatId,
                                          final
                                          UsernamePasswordAuthenticationToken
                                                  token) {
        if (userService.existsByEmail(token.getName())) {
            seatService
                    .checkInOnSeat(seatId, token.getName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
