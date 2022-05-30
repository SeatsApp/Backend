package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Point {
    /**
     * Represents the first point for rendering the walls of the svg.
     */
    private int firstPoint;

    /**
     * Represents the second point for rendering the walls of the svg.
     */
    private int secondPoint;
}
