package com.seatapp.services;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.domain.Seat;
import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    /**
     * get building by building id.
     *
     * @param buildingId the building id
     * @return the found building
     */
    @Override
    public Building getById(final long buildingId) {
        return buildingRepository.findById(buildingId);
    }

    /**
     * Creates a building.
     *
     * @param building the building that will be created.
     * @return the created building
     */
    @Override
    public Building createBuilding(final Building building) {
        return buildingRepository.save(building);
    }

    /**
     * Changes an existing by the parameter of the given building.
     * The building from the database is retrieved by the building id
     * from the parameter.
     *
     * @param changedBuilding the building with the changes
     *                        for the existing building
     * @return the changed building
     */
    @Override
    public Building updateBuilding(final Long buildingId,
                                   final Building changedBuilding) {
        Building existingBuilding =
                buildingRepository.findById(buildingId);
        existingBuilding.setName(changedBuilding.getName());

        List<Floor> newFloors = new ArrayList<>(
                getExistingFloors(changedBuilding, existingBuilding));
        newFloors.addAll(getNewFloors(changedBuilding));
        existingBuilding.setFloors(newFloors);

        return buildingRepository.save(existingBuilding);
    }

    /**
     * Gets the list of floors which still exist.
     *
     * @param changedBuilding  the changed building received
     * @param existingBuilding the existing building from the database
     * @return a list of the existing floors.
     */
    private List<Floor> getExistingFloors(final Building changedBuilding,
                                          final Building existingBuilding) {
        List<Floor> newFloors = new ArrayList<>();
        existingBuilding.getFloors().forEach(
                existingFloor -> changedBuilding.getFloors()
                        .stream().filter(filterFloor ->
                                filterFloor.getId() == existingFloor.getId())
                        .findFirst()
                        .ifPresent(floor -> {
                            existingFloor.setName(floor.getName());
                            existingFloor.setPoints(floor.getPoints());

                            newFloors.add(existingFloor);
                        }));
        return newFloors;
    }

    /**
     * Gets the list of new floors.
     *
     * @param changedBuilding the changed building received
     * @return a list of the new floors.
     */
    private List<Floor> getNewFloors(final Building changedBuilding) {
        return changedBuilding.getFloors()
                .stream().filter(filterFloor ->
                        filterFloor.getId() == 0)
                .toList();
    }
}
