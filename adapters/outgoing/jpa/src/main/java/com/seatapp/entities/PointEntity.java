package com.seatapp.entities;

import com.seatapp.domain.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "Point")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PointEntity {
    /**
     * Represents the first point for rendering the walls of the svg.
     */
    private int firstPoint;

    /**
     * Represents the second point for rendering the walls of the svg.
     */
    private int secondPoint;

    /**
     * This method converts a point to a pointEntity.
     *
     * @param point the to be converted point
     * @return a seat entity
     */
    public static PointEntity build(final Point point) {
        return new PointEntity(point.getFirstPoint(),
                point.getSecondPoint());
    }

    /**
     * This method converts a pointEntity to a point.
     *
     * @return a point
     */
    public Point toPoint() {
        return new Point(this.getFirstPoint(),
                this.getSecondPoint());
    }
}
