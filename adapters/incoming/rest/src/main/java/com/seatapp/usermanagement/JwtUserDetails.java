package com.seatapp.usermanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seatapp.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class JwtUserDetails implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The email of the JWT user.
     */
    private final String email;

    /**
     * The password of the JWT user.
     */
    @JsonIgnore
    private final String password;

    /**
     * The roles of the JWT user.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Create the JwtUserDetails.
     *
     * @param email       the email from the user
     * @param password    the encoded password from the user
     * @param authorities the role from the user
     */
    public JwtUserDetails(final String email,
                          final String password,
                          final Collection<?
                                  extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Build the JwtUserDetails.
     *
     * @param user the user that is used to create the object
     * @return a JwtUserDetails created from the user
     */
    public static JwtUserDetails build(final User user) {
        List<SimpleGrantedAuthority> authorities = Stream.of("Admin")
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtUserDetails(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    @Override
    public final Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    public final String getUsername() {
        return email;
    }

    @Override
    public final boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public final boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public final boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public final boolean isEnabled() {
        return true;
    }
}
