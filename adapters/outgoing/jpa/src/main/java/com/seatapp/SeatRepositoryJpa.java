package com.seatapp;

import com.seatapp.entities.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepositoryJpa extends
        JpaRepository<SeatEntity, Long> {
}
