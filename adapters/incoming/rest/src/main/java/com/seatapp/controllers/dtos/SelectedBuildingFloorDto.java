package com.seatapp.controllers.dtos;

import com.seatapp.domain.Building;
import com.seatapp.domain.Floor;
import com.seatapp.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedBuildingFloorDto {
    /**
     * Represents the building id.
     */
    private Long buildingId;
    /**
     * Represents the building name.
     */
    private String buildingName;

    /**
     * Represents the floor id.
     */
    private Long floorId;
    /**
     * Represents the floor name.
     */
    private String floorName;

    /**
     * Represents the floor points for creating the walls in a svg.
     */
    private List<PointDto> floorPoints;

    /**
     * Represents the seats from the building and floor.
     */
    private List<SeatDto> seats;

    /**
     * This method converts a building to a buildingDto.
     *
     * @param building the to be converted building
     * @return a building dto
     */
    public static SelectedBuildingFloorDto build(final Building building) {
        Floor floor = building.getFloors().stream().findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Floor does not exist"));

        return new SelectedBuildingFloorDto(building.getId(),
                building.getName(),
                floor.getId(),
                floor.getName(),
                floor.getPoints().stream()
                        .map(PointDto::build)
                        .toList(),
                floor.getSeats().stream()
                        .map(SeatDto::build)
                        .toList());
    }

    /**
     * This method converts a building to a buildingDto.
     *
     * @param building the to be converted building
     * @param date     the date for determining the seat status
     * @return a building dto
     */
    public static SelectedBuildingFloorDto build(final Building building,
                                                 final LocalDate date) {
        Floor floor = building.getFloors().stream().findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Floor does not exist"));

        return new SelectedBuildingFloorDto(building.getId(),
                building.getName(),
                floor.getId(),
                floor.getName(),
                floor.getPoints().stream()
                        .map(PointDto::build)
                        .toList(),
                floor.getSeats().stream()
                        .map(seat -> SeatDto.build(seat,
                                date.atTime(0, 0),
                                date.plusDays(1).atTime(0, 0)))
                        .toList());
    }
}
