package com.seatapp.controllers;

import com.seatapp.controllers.dtos.UserDto;
import com.seatapp.domain.User;
import com.seatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * This is an api to adjust users.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
    /**
     * Represents the user service that is called.
     */
    private final UserService userService;

    /**
     * Creates the controller with a specified service.
     *
     * @param userService the userService
     */
    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Changes the role of a user.
     * @param user the information of the to be changed user.
     * @param token the information of the user who
     *             wants to change the role.
     * @return Returns a response with the HttpStatus
     * and a message.
     */
    @PatchMapping("role")
    public ResponseEntity<String> changeUserRole(
            @RequestBody final UserDto user,
            final UsernamePasswordAuthenticationToken
            token) {
        userService.changeUserRole(new User(user.getEmail(), "",
                user.getFullName(), user.getRole()), token.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * Gets all the users from database.
     *
     * @return a list of user
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> foundUsers = userService.getAll();

        List<UserDto> userDtos = foundUsers.stream()
                .map(UserDto::build)
                .toList();
        return ResponseEntity.ok(userDtos);
    }
}
