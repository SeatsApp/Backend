package com.seatapp.repositories;

import com.seatapp.entities.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepositoryJpa extends
        JpaRepository<BuildingEntity, Long> {
}
