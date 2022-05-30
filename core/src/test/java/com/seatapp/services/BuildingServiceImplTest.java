package com.seatapp.services;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.repositories.BuildingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BuildingServiceImplTest {
    /**
     * Represents the building repository.
     */
    @MockBean(name = "buildingRepositoryImpl")
    private BuildingRepository buildingRepository;

    /**
     * Represents the building service.
     */
    @Autowired
    private BuildingServiceImpl buildingService;

    @Test
    void getBuildingByidAndFloorId() {
        // Arrange
        when(buildingRepository.findById(1L))
                .thenReturn(new Building(1L, "Building 1",
                        List.of(new Floor(2L, "Floor 1",
                                new ArrayList<>(), new ArrayList<>()))));

        //Act
        Building building = buildingService.getByIdAndFloorId(1L, 2L);

        //Assert
        assertEquals("Building 1", building.getName());
        assertEquals("Floor 1", building.getFloors().get(0).getName());
    }

    @Test
    void getBuildingByidAndFloorIdAndDate() {
        // Arrange
        Reservation reservation = new Reservation(
                LocalDate.now().atTime(0, 0),
                LocalDate.now().plusDays(1).atTime(0, 0),
                new User());

        Seat seat = new Seat("A1");
        seat.setReservations(List.of(reservation));

        when(buildingRepository.findById(1L))
                .thenReturn(new Building(1L, "Building 1",
                        List.of(new Floor(2L, "Floor 1",
                                new ArrayList<>(), List.of(seat)))));

        //Act
        Building building = buildingService.getByIdAndFloorIdAndDate(
                1L, 2L, LocalDate.now());

        //Assert
        assertEquals("Building 1", building.getName());
        assertEquals("Floor 1", building.getFloors().get(0).getName());
        assertEquals("A1", building.getFloors()
                .get(0).getSeats().get(0).getName());
    }
}
