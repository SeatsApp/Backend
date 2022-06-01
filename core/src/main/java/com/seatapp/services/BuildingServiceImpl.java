package com.seatapp.services;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;
import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    /**
     * Represents the building repository.
     */
    private final BuildingRepository buildingRepository;

    /**
     * Creates a service with the specified repository.
     *
     * @param buildingRepository The building repository.
     */
    @Autowired
    public BuildingServiceImpl(final BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    /**
     * Get all the buildings.
     *
     * @return a list of the found buildings
     */
    @Override
    public List<Building> getAll() {
        return buildingRepository.findAll();
    }

    /**
     * Gets the building by building id, floor id and
     * the reservations by date.
     *
     * @param buildingId the building id
     * @param floorId    the floor id
     * @param date       the date for filtering the reservations
     * @return the found building
     */
    @Override
    public Building getByIdAndFloorIdAndDate(final long buildingId,
                                             final long floorId,
                                             final LocalDate date) {
        Building building = buildingRepository.findById(buildingId);
        Floor floor = building.getFloors().stream()
                .filter(filterFloor -> filterFloor.getId() == floorId)
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "This floor does not exist."));

        List<Seat> seats = floor.getSeats();
        for (Seat s : seats) {
            s.setReservations(s.getReservations().stream()
                    .filter(reservation -> reservation.getStartDateTime()
                            .toLocalDate().equals(date))
                    .filter(reservation -> !reservation.isCancelled())
                    .toList());
        }

        floor.setSeats(seats);
        building.setFloors(List.of(floor));

        return building;
    }

    /**
     * Gets the building by building id and floor id.
     *
     * @param buildingId the building id
     * @param floorId    the floor id
     * @return the found building
     */
    @Override
    public Building getByIdAndFloorId(final long buildingId,
                                      final long floorId) {
        Building building = buildingRepository.findById(buildingId);
        Floor floor = building.getFloors().stream()
                .filter(filterFloor -> filterFloor.getId() == floorId)
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "This floor does not exist."));

        building.setFloors(List.of(floor));

        return building;
    }
}
