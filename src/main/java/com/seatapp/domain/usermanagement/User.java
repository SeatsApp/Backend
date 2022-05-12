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
