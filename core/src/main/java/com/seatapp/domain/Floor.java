package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Floor {
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
    private List<Point> points;

    /**
     * Represents the seats on the floor.
     */
    private List<Seat> seats;
}
