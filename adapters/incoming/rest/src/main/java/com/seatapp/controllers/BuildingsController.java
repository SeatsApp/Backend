package com.seatapp.controllers;

import com.seatapp.controllers.dtos.BuildingDto;
import com.seatapp.controllers.dtos.SelectedBuildingDto;
import com.seatapp.domain.Building;
import com.seatapp.services.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
     * Get the selected building by building id and floor id with optionally
     * the reservations from the given date.
     *
     * @param buildingId the building id of the seats you want to find
     * @param floorId    the floor id of the seats you want to find
     * @param date       the date where you want reservations from.
     * @return Returns a responseEntity with the HttpStatus
     * and the selected building.
     */
    @GetMapping("{buildingId}")
    public ResponseEntity<SelectedBuildingDto> getBuildingByIdAndFloorId(
            @PathVariable final long buildingId,
            @RequestParam final long floorId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) final LocalDate date) {
        SelectedBuildingDto buildingDto;
        if (date == null) {
            Building building = buildingService
                    .getByIdAndFloorId(buildingId, floorId);
            buildingDto = SelectedBuildingDto.build(building);
        } else {
            Building building = buildingService
                    .getByIdAndFloorIdAndDate(buildingId, floorId, date);
            buildingDto = SelectedBuildingDto.build(building, date);
        }

        return ResponseEntity.ok(buildingDto);
    }
}
