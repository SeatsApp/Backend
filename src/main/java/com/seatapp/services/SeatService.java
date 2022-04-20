package com.seatapp.services;

import com.seatapp.controllers.dtos.SeatDto;
import com.seatapp.domain.Seat;
import com.seatapp.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class SeatService {
    /**
     * Represents the seat repository.
     */
    private final SeatRepository seatRepository;

    /**
     * Creates a service with the specified repository.
     * @param seatRepository The seat repository.
     */
    @Autowired
    public SeatService(final SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Saves a seat to the database.
     * @param seatDto The seatDto containing the name.
     * @return The saved seat.
     */
    public Seat createSeat(final SeatDto seatDto) {
        if (seatDto == null) {
            throw new IllegalArgumentException("SeatDto cannot be null");
        }
        if (seatDto.getName() == null || seatDto.getName().isBlank()) {
            throw new IllegalArgumentException("The seat name is invalid.");
        }
        return seatRepository.save(new Seat(seatDto.getName()));
    }

    /**
     * Deletes the seat with the specified id.
     * @param seatId the id of the to be deleted seat.
     * @return the deleted seat.
     */
    public Seat delete(final Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No seat with this id."));
        seatRepository.delete(seat);
        return seat;
    }
}
