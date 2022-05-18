package com.seatapp;

import com.seatapp.domain.Seat;
import com.seatapp.entities.SeatEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SeatRepositoryImpl implements SeatRepository {
    /**
     * The seat repository.
     */
    private final SeatRepositoryJpa repository;

    /**
     * Creates the SeatRepositoryImpl.
     * @param repository the repository.
     */
    @Autowired
    public SeatRepositoryImpl(final SeatRepositoryJpa repository) {
        this.repository = repository;
    }

    /**
     * Saves a seat to the database.
     * @param seat the seat you want to save
     * @return the saved seat
     */
    @Override
    public Seat save(final Seat seat) {
        SeatEntity toBeSavedEntity = SeatEntity.build(seat);
        SeatEntity savedEntity = repository.save(toBeSavedEntity);

        return savedEntity.toSeat();
    }

    /**
     * Deletes a seat from the database.
     * @param seatId id of the seat that will be deleted
     */
    @Override
    public void deleteById(final Long seatId) {
        SeatEntity seat = repository.findById(seatId)
                .orElseThrow(() ->
                        new EntityNotFoundException("no seat with this id."));
        repository.delete(seat);
    }

    /**
     * Finds all the existing seats.
     * @return a list of all the seats
     */
    @Override
    public List<Seat> findAll() {
        List<SeatEntity> entities = repository.findAll();

        return entities.stream().map(SeatEntity::toSeat)
                .collect(Collectors.toList());
    }

    /**
     * Find a seat by the given id.
     * @param seatId the id of the seat you want to find
     * @return The found seat else error.
     */
    @Override
    public Seat findById(final Long seatId) {
        Optional<SeatEntity> optionalEntity = repository.findById(seatId);
        return optionalEntity.map(SeatEntity::toSeat).orElseThrow(() ->
                new EntityNotFoundException("No seat with this id."));
    }

    /**
     * Deletes all the seats from the database.
     */
    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
