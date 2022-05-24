package com.seatapp.entities;

import com.seatapp.domain.Role;
import com.seatapp.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_user")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UserEntity {
    /**
     * The email of the user.
     */
    @Id
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

    /**
     * This method converts a user to a userEntity.
     * @param user the to be converted user
     * @return a user entity
     */
    public static UserEntity build(final User user) {
        return new UserEntity(user.getEmail(), user.getFullName(),
                user.getPassword(), user.getRole());
    }

    /**
     * This method converts a userEntity to a user.
     * @return a user
     */
    public User toUser() {
        return new User(this.getEmail(),
                this.getFullName(), this.getPassword(), this.getRole());
    }
}
