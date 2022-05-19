package com.seatapp.repositories;

import com.seatapp.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepositoryJpa extends
        JpaRepository<ReservationEntity, Long> {
}
