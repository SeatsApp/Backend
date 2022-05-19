package com.seatapp.repositories;

import com.seatapp.domain.Seat;
import com.seatapp.entities.SeatEntity;
import com.seatapp.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SeatRepositoryImpl implements SeatRepository {
    /**
     * Loading in the data for an island of 4 seats.
     */
    private static final int ISLAND_SEAT4 = 5;

    /**
     * Loading in the data for an island of 6 seats.
     */
    private static final int ISLAND_SEAT6 = 7;


    /**
     * The seat repository.
     */
    private final SeatRepositoryJpa repository;

    /**
     * Creates the SeatRepositoryImpl.
     *
     * @param repository the repository.
     */
    @Autowired
    public SeatRepositoryImpl(final SeatRepositoryJpa repository) {
        this.repository = repository;
        initEntities();
    }

    /**
     * Initialize the static floorplan.
     */
    private void initEntities() {
        for (int i = 1; i < ISLAND_SEAT4; i++) {
            save(new Seat("A" + i));
            save(new Seat("B" + i));
            save(new Seat("C" + i));
            save(new Seat("D" + i));
        }

        for (int i = 1; i < ISLAND_SEAT6; i++) {
            save(new Seat("E" + i));
            save(new Seat("F" + i));
            save(new Seat("G" + i));
            save(new Seat("H" + i));
            save(new Seat("I" + i));
            save(new Seat("J" + i));
            save(new Seat("K" + i));
        }
    }

    /**
     * Saves a seat to the database.
     *
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
     *
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
     *
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
     *
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
