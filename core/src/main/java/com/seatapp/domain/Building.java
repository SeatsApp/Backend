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
public class Building {
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
    private List<Floor> floors;
}
