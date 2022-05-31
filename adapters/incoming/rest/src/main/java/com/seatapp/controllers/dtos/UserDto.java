package com.seatapp.controllers.dtos;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    /**
     * Represents the email of the user.
     */
    private String email;

    /**
     * Represents the fullName of the user.
     */
    private String fullName;

    /**
     * Represents the role of the user.
     */
    private Role role;

    /**
     * This method converts a user to a userDto.
     *
     * @param user the to be converted user
     * @return a user dto
     */
    public static UserDto build(final User user) {
        return new UserDto(
                user.getEmail(), user.getFullName(), user.getRole());
    }
}
