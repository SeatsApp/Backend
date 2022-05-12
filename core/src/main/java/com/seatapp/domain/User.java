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
    private String role;

    /**
     * Creates a user.
     *
     * @param email the email of the user
     * @param fullName the full name of the user
     * @param password the password of the user
     */
    public User(final String email, final String fullName,
                final String password) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = "Admin";
    }
}
