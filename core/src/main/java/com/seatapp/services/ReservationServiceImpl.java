package com.seatapp.services;

import com.seatapp.domain.Reservation;
import com.seatapp.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {

    /**
     * Represents the reservation repository.
     */
    private final ReservationRepository reservationRepository;

    /**
     * Creates a service with the specified repository.
     *
     * @param reservationRepository The reservation repository.
     */
    @Autowired
    public ReservationServiceImpl(
            final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Cancels a reservation by setting
     * the cancel value to true.
     *
     * @param reservationId the reservation id of which
     *                      it will be cancelled
     * @param email         the email of the user who made
     *                      a reservation
     */
    @Override
    public void cancelReservation(final long reservationId,
                                  final String email) {
        Reservation reservation = reservationRepository.findById(reservationId);
        reservation.setCancelled(true);
        reservationRepository.save(reservation);
    }
}
