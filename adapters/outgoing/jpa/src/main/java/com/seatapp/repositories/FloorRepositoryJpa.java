package com.seatapp.repositories;

import com.seatapp.entities.FloorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorRepositoryJpa extends
        JpaRepository<FloorEntity, Long> {
}
