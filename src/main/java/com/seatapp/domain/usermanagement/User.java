package com.seatapp.domain.usermanagement;

import com.seatapp.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * The email of the user.
     */
    @Id
    @Column(name = "email")
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
     * The users' reservations.
     */
    @OneToMany
    @JoinColumn(name = "email")
    private List<Reservation> reservations;

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
