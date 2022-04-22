package com.seatapp.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SeatDto {
    /**
     * Represents the seats' name.
     */
    private String name;
}
