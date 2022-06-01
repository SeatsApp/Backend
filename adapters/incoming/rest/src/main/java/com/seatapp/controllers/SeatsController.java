package com.seatapp.controllers;

import com.seatapp.controllers.dtos.ReservationDto;
import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.services.FloorService;
import com.seatapp.services.SeatService;
import com.seatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * Represents the floor service that is called.
     */
    private final FloorService floorService;

    /**
     * Creates the controller with a specified service.
     *
     * @param seatService  the seatService
     * @param userService  the userService
     * @param floorService the floorService
     */
    @Autowired
    public SeatsController(final SeatService seatService,
                           final UserService userService,
                           final FloorService floorService) {
        this.seatService = seatService;
        this.userService = userService;
        this.floorService = floorService;
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
        Seat seat = new Seat(seatDto.getName(), seatDto.getXCoordinates(),
                seatDto.getYCoordinates(), seatDto.getWidth(),
                seatDto.getHeight());
        Floor floor = floorService.findById(seatDto.getFloorId());
        Seat createdSeat = seatService.createSeat(seat);
        floorService.addSeat(floor, createdSeat);
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
     * Makes a seat unavailable for reservations.
     * @param seatId the id of the seat that has to be changed.
     * @return Returns a responseEntity with the HttpStatus and a message.
     */
    @PatchMapping("{seatId}/availability")
    public ResponseEntity<String> changeAvailability(
            @PathVariable final Long seatId) {
        seatService.changeAvailability(seatId);
        return ResponseEntity.ok().build();
    }

    /**
     * Takes all the seats from the database.
     *
     * @return Returns a responseEntity with the HttpStatus and the found seats.
     */
    @GetMapping
    public ResponseEntity<List<SeatDto>> getSeats() {
        List<Seat> foundSeats = seatService.getAll();
        List<SeatDto> seatDtos = foundSeats.stream()
                .map(SeatDto::build)
                .toList();
        return ResponseEntity.ok(seatDtos);
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
    public ResponseEntity<Long>
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
        Seat seat = seatService.reserve(seatId, reservation);

        int lastIndex = seat.getReservations().size() - 1;
        return ResponseEntity.ok(seat.getReservations().get(lastIndex).getId());
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
