package com.seatapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * The email of the user.
     */
    private String email;

    /**
     * The encoded password of the user.
     */
    private String password;

    /**
     * The full name of the user.
     */
    private String fullName;

    /**
     * The role of the user.
     */
    private Role role;
}
