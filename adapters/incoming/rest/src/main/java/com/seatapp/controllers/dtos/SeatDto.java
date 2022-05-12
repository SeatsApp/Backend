package com.seatapp.controllers.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SeatDto {
    /**
     * Represents the seats' name.
     */
    private String name;

    /**
     * Creates an seatdto with a name.
     * @param name name of the seatdto.
     */
    public SeatDto(final String name) {
        this.name = name;
    }
}
