package com.seatapp.controllers;

import com.seatapp.controllers.dtos.BuildingDto;
import com.seatapp.controllers.dtos.FloorDto;
import com.seatapp.controllers.dtos.SelectedBuildingFloorDto;
import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/buildings")
public class BuildingsController {
    /**
     * Represents the building service that is called.
     */
    private final BuildingService buildingService;

    /**
     * Creates a controller with the specified service.
     *
     * @param buildingService represents the building service
     */
    @Autowired
    public BuildingsController(final BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * create a new building.
     *
     * @param buildingDto the building dto
     *                    to create the new building
     * @return Returns a responseEntity with the HttpStatus
     * and the created building uri.
     */
    @PostMapping
    public ResponseEntity<String> createBuilding(
            @RequestBody final BuildingDto buildingDto) {
        List<Floor> floors = buildingDto.getFloors()
                .stream().map(FloorDto::toFloor).toList();
        Building building = new Building(buildingDto.getId(),
                buildingDto.getName(),
                floors);
        building = buildingService.createBuilding(building);

        return ResponseEntity.created(
                URI.create("/building/" + building.getId())).build();
    }

    /**
     * change an existing building.
     *
     * @param buildingId  the building id of the building
     *                    that will be updated
     * @param buildingDto the building dto to change the building
     * @return Returns a responseEntity with the HttpStatus.
     */
    @PatchMapping("{buildingId}")
    public ResponseEntity<String> changeBuilding(
            @PathVariable final Long buildingId,
            @RequestBody final BuildingDto buildingDto) {
        List<Floor> floors = buildingDto.getFloors()
                .stream().map(FloorDto::toFloor).toList();
        Building building = new Building(buildingDto.getId(),
                buildingDto.getName(),
                floors);
        buildingService.updateBuilding(buildingId, building);

        return ResponseEntity.ok().build();
    }

    /**
     * Get the all the buildings from the database.
     *
     * @return Returns a responseEntity with the HttpStatus
     * and the found buildings.
     */
    @GetMapping
    public ResponseEntity<List<BuildingDto>> getBuildings() {

        List<Building> buildings = buildingService
                .getAll();

        List<BuildingDto> buildingDtos = buildings.stream()
                .map(BuildingDto::build).toList();

        return ResponseEntity.ok(buildingDtos);
    }

    /**
     * Get the selected building by building id.
     *
     * @param buildingId the building id of the seats you want to find
     * @return Returns a responseEntity with the HttpStatus
     * and the selected building.
     */
    @GetMapping("{buildingId}")
    public ResponseEntity<BuildingDto> getBuildingById(
            @PathVariable final long buildingId) {

        Building building = buildingService
                .getById(buildingId);

        BuildingDto buildingDto = BuildingDto.build(building);

        return ResponseEntity.ok(buildingDto);
    }

    /**
     * Get the selected building and floor by building id and floor id
     * with optionally the reservations from the given date.
     *
     * @param buildingId the building id of the seats you want to find
     * @param floorId    the floor id of the seats you want to find
     * @param date       the date where you want reservations from.
     * @return Returns a responseEntity with the HttpStatus
     * and the selected building and the selected Floor.
     */
    @GetMapping("{buildingId}/floors/{floorId}")
    public ResponseEntity<SelectedBuildingFloorDto> getBuildingByIdAndFloorId(
            @PathVariable final long buildingId,
            @PathVariable final long floorId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) final LocalDate date) {
        SelectedBuildingFloorDto buildingDto;
        if (date == null) {
            Building building = buildingService
                    .getByIdAndFloorId(buildingId, floorId);
            buildingDto = SelectedBuildingFloorDto.build(building);
        } else {
            Building building = buildingService
                    .getByIdAndFloorIdAndDate(buildingId, floorId, date);
            buildingDto = SelectedBuildingFloorDto.build(building, date);
        }

        return ResponseEntity.ok(buildingDto);
    }
}
