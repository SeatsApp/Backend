package com.seatapp.domain.usermanagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The encoded password of the user.
     */
    private String password;

    /**
     * The email of the user.
     */
    private String email;

    /**
     * The role of the user.
     */
    private String role;

    /**
     * Creates a user.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(final String username, final String email,
                final String password) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "Admin";
    }
}
