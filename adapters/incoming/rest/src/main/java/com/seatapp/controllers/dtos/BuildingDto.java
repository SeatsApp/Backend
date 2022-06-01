package com.seatapp.controllers.dtos;

import com.seatapp.domain.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDto {
    /**
     * Represents the building id.
     */
    private Long id;

    /**
     * Represents the building name.
     */
    private String name;

    /**
     * Represents the floors in the building.
     */
    private List<FloorDto> floors;

    /**
     * This method converts a building to a buildingDto.
     *
     * @param building the to be converted building
     * @return a floor dto
     */
    public static BuildingDto build(final Building building) {
        return new BuildingDto(building.getId(),
                building.getName(),
                building.getFloors().stream()
                        .map(FloorDto::build).toList());
    }
}
