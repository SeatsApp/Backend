package com.seatapp.controllers.dtos;

import com.seatapp.domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorDto {
    /**
     * Represents the floor id.
     */
    private long id;

    /**
     * Represents the floor name.
     */
    private String name;

    /**
     * Represents the points for drawing the walls of the floor in a svg.
     */
    private List<PointDto> points;

    /**
     * Represents the seats on the floor.
     */
    private List<SeatDto> seats;

    /**
     * This method converts a floor to a floorDto.
     *
     * @param floor the to be converted floor
     * @return a floor dto
     */
    public static FloorDto build(final Floor floor) {
        return new FloorDto(floor.getId(),
                floor.getName(),
                floor.getPoints().stream()
                        .map(PointDto::build).toList(),
                floor.getSeats().stream()
                        .map(SeatDto::build).toList());
    }

    /**
     * This method converts a floorDto to a floor.
     *
     * @return a floor
     */
    public Floor toFloor() {
        return new Floor(this.getId(), this.getName(),
                this.getPoints().stream()
                        .map(PointDto::toPoint)
                        .toList(),
                new ArrayList<>());
    }
}
