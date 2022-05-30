package com.seatapp.repositories;

import com.seatapp.domain.Seat;
import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BuildingRepositoryTests {
    /**
     * Building used in tests.
     */
    private Building validBuilding;
    /**
     * The buildingRepositoryImplementation.
     */
    @Autowired
    private BuildingRepositoryImpl buildingRepository;

    @BeforeEach
    void setup() {
        buildingRepository.deleteAll();

        Seat seat = new Seat("Test");
        seat.setReservations(List.of());

        Floor floor = new Floor(2L, "Floor 1",
                List.of(), List.of(seat));

        validBuilding = new Building(1L, "Building 1",
                List.of(floor));
    }

    @Test
    @Transactional
    void findAllBuildings() {
        //Arrange
        Seat seat = new Seat("A2");
        seat.setReservations(List.of());

        Floor floor = new Floor(2L, "Floor 2",
                new ArrayList<>(), List.of(seat));

        Building building = new Building(1L, "Building 2",
                List.of(floor));

        buildingRepository.save(validBuilding);
        buildingRepository.save(building);

        //Act
        List<Building> foundBuilding = buildingRepository.findAll();

        //Assert
        assertEquals(2, foundBuilding.size());
        assertEquals(validBuilding.getName(), foundBuilding.get(0).getName());
        assertEquals("Building 2", foundBuilding.get(1).getName());
    }

    @Test
    @Transactional
    void findBuildingById() {
        //Arrange
        Building savedBuilding = buildingRepository.save(validBuilding);

        //Act
        Building foundBuilding = buildingRepository
                .findById(savedBuilding.getId());

        //Assert
        assertNotNull(foundBuilding);
        assertEquals(savedBuilding.getId(), foundBuilding.getId());
    }
}
