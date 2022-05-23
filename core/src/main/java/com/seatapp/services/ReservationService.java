package com.seatapp.services;


public interface ReservationService {
    /**
     * Cancels a reservation by setting
     * the cancel value to true.
     *
     * @param reservationId the reservation id of which
     *                      it will be cancelled
     * @param email         the email of the user who made
     *                      a reservation
     */
    void cancelReservation(long reservationId, String email);
}
