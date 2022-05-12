package com.seatapp.repositories;

import com.seatapp.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepositoryImpl extends
        JpaRepository<Seat, Long>, SeatRepository {
}
