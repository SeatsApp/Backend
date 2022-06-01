package com.seatapp.controllers.dtos;

import com.seatapp.domain.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {
    /**
     * Represents the first point for rendering the walls of the svg.
     */
    private int firstPoint;

    /**
     * Represents the second point for rendering the walls of the svg.
     */
    private int secondPoint;

    /**
     * This method converts a point to a pointDto.
     *
     * @param point the to be converted point
     * @return a point dto
     */
    public static PointDto build(final Point point) {
        return new PointDto(point.getFirstPoint(),
                point.getSecondPoint());
    }
}
