package com.seatapp.services;

import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class FloorServiceImpl implements FloorService {
    /**
     * Represents the building repository.
     */
    private final FloorRepository floorRepository;

    /**
     * Creates a service with the specified repository.
     *
     * @param floorRepository the floor repository
     */
    @Autowired
    FloorServiceImpl(final FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    /**
     * Adds the seat to the floor.
     *
     * @param floor the floor on which the seat will be added
     * @param seat  the seat that will be added to the floor
     * @return the floor with the added seat
     */
    @Override
    public Floor addSeat(final Floor floor,
                         final Seat seat) {
        List<Seat> seats = new ArrayList<>(floor.getSeats());
        seats.add(seat);
        floor.setSeats(seats);
        return floorRepository.save(floor);
    }

    /**
     * Get the floor by the id.
     *
     * @param floorId the floor id
     * @return the found floor
     */
    @Override
    public Floor findById(final long floorId) {
        return floorRepository.findById(floorId);
    }
}
