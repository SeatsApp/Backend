package com.seatapp.services;

import com.seatapp.domain.User;
import com.seatapp.repositories.UserRepository;
import com.seatapp.usermanagement.services.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceTest {
    /**
     * Represents the user repository.
     */
    @MockBean(name = "userRepositoryImpl")
    private UserRepository userRepository;

    /**
     * Represents the user service.
     */
    @Autowired
    private LoginServiceImpl loginService;

    /**
     * Represents the authentication manager.
     */
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void loginUserDoesNotExistTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, password);

        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);
        OAuth2User user = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                Map<String, Object> map = new HashMap<>();
                map.put("preferred_username", email);
                return map;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(
                        "APPROLE_Admin"));
            }

            @Override
            public String getName() {
                return fullName;
            }
        };

        // Act
        Authentication receivedAuthentication = loginService.login(user);

        // Assert
        assertEquals(email, receivedAuthentication.getName());
    }

    @Test
    void loginUserDoesExistTest() {
        // Arrange
        String fullName = "Thomas";
        String email = "test@hotmail.com";
        String password = "Test";
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(email, password);
        OAuth2User user = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                Map<String, Object> map = new HashMap<>();
                map.put("preferred_username", email);
                return map;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(
                        "APPROLE_Admin"));
            }

            @Override
            public String getName() {
                return fullName;
            }
        };

        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.<User>getArgument(0));
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(authenticationManager.authenticate(authentication))
                .thenReturn(authentication);

        // Act
        Authentication receivedAuthentication =
                loginService.login(user);

        // Assert
        assertEquals(email, receivedAuthentication.getName());
    }
}
