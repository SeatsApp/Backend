package com.seatapp.controllers;

import com.seatapp.controllers.dtos.UserReservationDto;
import com.seatapp.domain.Seat;
import com.seatapp.services.ReservationService;
import com.seatapp.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * This is an api for the reservations.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/reservations")
public class ReservationsController {

    /**
     * Represents the reservation service.
     */
    private final ReservationService reservationService;

    /**
     * Represents the seat service.
     */
    private final SeatService seatService;

    /**
     * Creates a controller with the specified service.
     *
     * @param reservationService the reservation service
     * @param seatService        the seat service
     */
    @Autowired
    public ReservationsController(
            final ReservationService reservationService,
            final SeatService seatService) {
        this.reservationService = reservationService;
        this.seatService = seatService;
    }

    /**
     * Cancels a reservation.
     *
     * @param reservationId the reservation id of which will be cancelled
     * @param token         the user who requested the cancellation
     * @return a http response
     */
    @PatchMapping("{reservationId}/cancel")
    public ResponseEntity<String>
    cancelReservation(@PathVariable final Long reservationId,
                      final
                      UsernamePasswordAuthenticationToken
                              token) {
        reservationService.cancelReservation(reservationId, token.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * /**
     * Takes all the seats from the database with the
     * reservations by the user who reserved them.
     * The seats will be converted to a UserReservationDto.
     *
     * @param token this contains the email of the user
     *              who send the request
     * @return Returns a responseEntity with the HttpStatus
     * and the found seats.
     */
    @GetMapping("users")
    public ResponseEntity<List<UserReservationDto>>
    getReservationsOfTheUser(
            final UsernamePasswordAuthenticationToken token) {
        List<Seat> foundSeats = seatService
                .getAllByUser(token.getName());

        List<UserReservationDto> dtoList =
                UserReservationDto.buildList(foundSeats);

        dtoList.sort((o1, o2) -> o1.getStartDateTime()
                .isBefore(o2.getStartDateTime()) ? 1 : 0);

        return ResponseEntity.ok(dtoList);
    }
}
