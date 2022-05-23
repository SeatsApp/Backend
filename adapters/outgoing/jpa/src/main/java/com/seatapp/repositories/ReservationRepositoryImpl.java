package com.seatapp.repositories;

import com.seatapp.domain.Reservation;
import com.seatapp.entities.ReservationEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationRepositoryImpl implements ReservationRepository {
    /**
     * Represents the reservation repository.
     */
    private final ReservationRepositoryJpa repository;


    /**
     * Creates the ReservationRepositoryImpl.
     *
     * @param repository the repository.
     */
    @Autowired
    public ReservationRepositoryImpl(
            final ReservationRepositoryJpa repository) {
        this.repository = repository;
    }

    /**
     * Saving the reservation in the database.
     *
     * @param reservation the reservation you want to save
     * @return The updated reservation due to
     * the changes from the database
     */
    @Override
    public Reservation save(final Reservation reservation) {
        ReservationEntity toBeSavedEntity =
                ReservationEntity.build(reservation);
        ReservationEntity reservationEntity =
                repository.save(toBeSavedEntity);

        return reservationEntity.toReservation();
    }

    /**
     * Tries to find the reservation with the given id.
     *
     * @param reservationId the id of the reservation
     *                      you want to find
     * @return the reservation with the given id.
     */
    @Override
    public Reservation findById(final Long reservationId) {
        Optional<ReservationEntity> optionalEntity =
                repository.findById(reservationId);
        return optionalEntity
                .map(ReservationEntity::toReservation)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "No reservation with this id."));
    }

    /**
     * Deletes all the reservations.
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
