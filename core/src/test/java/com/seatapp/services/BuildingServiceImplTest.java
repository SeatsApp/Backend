package com.seatapp.services;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Reservation;
import com.seatapp.domain.Seat;
import com.seatapp.domain.User;
import com.seatapp.repositories.BuildingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

    /**
     * Loading in the data for the building name.
     */
    private static final String BUILDING_NAME1 = "Building 1";

    /**
     * Loading in the data for the floor name.
     */
    private static final String FLOOR_NAME1 = "Floor 1";

    @Test
    void getBuildingByidAndFloorId() {
        // Arrange
        when(buildingRepository.findById(1L))
                .thenReturn(new Building(1L, BUILDING_NAME1,
                        List.of(new Floor(2L, FLOOR_NAME1,
                                new ArrayList<>(), new ArrayList<>()))));

        //Act
        Building building = buildingService.getByIdAndFloorId(1L, 2L);

        //Assert
        assertEquals(BUILDING_NAME1, building.getName());
        assertEquals(FLOOR_NAME1, building.getFloors().get(0).getName());
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
                .thenReturn(new Building(1L, BUILDING_NAME1,
                        List.of(new Floor(2L, FLOOR_NAME1,
                                new ArrayList<>(), List.of(seat)))));

        //Act
        Building building = buildingService.getByIdAndFloorIdAndDate(
                1L, 2L, LocalDate.now());

        //Assert
        assertEquals(BUILDING_NAME1, building.getName());
        assertEquals(FLOOR_NAME1, building.getFloors().get(0).getName());
        assertEquals("A1", building.getFloors()
                .get(0).getSeats().get(0).getName());
    }

    @Test
    void createBuilding() {
        // Arrange
        Reservation reservation = new Reservation(
                LocalDate.now().atTime(0, 0),
                LocalDate.now().plusDays(1).atTime(0, 0),
                new User());

        Seat seat = new Seat("A1");
        seat.setReservations(List.of(reservation));

        when(buildingRepository.save(Mockito.any(Building.class)))
                .thenAnswer(i -> {
                    Building building = i.getArgument(0);
                    building.setId(1L);
                    return building;
                });

        //Act
        Building building = buildingService.createBuilding(
                new Building(1L, BUILDING_NAME1,
                        List.of(new Floor(2L, FLOOR_NAME1,
                                new ArrayList<>(), List.of(seat)))));

        //Assert
        assertEquals(BUILDING_NAME1, building.getName());
        assertEquals(FLOOR_NAME1, building.getFloors().get(0).getName());
        assertEquals("A1", building.getFloors()
                .get(0).getSeats().get(0).getName());
    }

    @Test
    void updateBuilding() {
        // Arrange
        Reservation reservation = new Reservation(
                LocalDate.now().atTime(0, 0),
                LocalDate.now().plusDays(1).atTime(0, 0),
                new User());

        Seat seat = new Seat("A1");
        seat.setReservations(List.of(reservation));

        Floor floor1 = new Floor(2L, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));
        Floor floor2 = new Floor(0, FLOOR_NAME1,
                new ArrayList<>(), List.of(seat));

        when(buildingRepository.save(Mockito.any(Building.class)))
                .thenAnswer(i -> {
                    Building building = i.getArgument(0);
                    building.setId(1L);
                    return building;
                });

        when(buildingRepository.findById(1L))
                .thenReturn(new Building(1L, BUILDING_NAME1,
                        List.of(floor1)));

        //Act
        Building building = buildingService.updateBuilding(1L,
                new Building(1L, "Building 4",
                        List.of(floor1, floor2)));

        //Assert
        assertEquals("Building 4", building.getName());
        assertEquals(FLOOR_NAME1, building.getFloors().get(0).getName());
        assertEquals(2, building.getFloors().size());
        assertEquals("A1", building.getFloors()
                .get(0).getSeats().get(0).getName());
    }
}
