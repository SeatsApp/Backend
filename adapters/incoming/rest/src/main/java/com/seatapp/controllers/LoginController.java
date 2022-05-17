package com.seatapp.controllers;

import com.seatapp.services.UserService;
import com.seatapp.usermanagement.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("api")
public class LoginController {
    /**
     * The logger of the login controller.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoginController.class);

    /**
     * Represents the user service.
     */
    private final UserService userService;

    /**
     * Represents the user service.
     */
    private final JwtService jwtService;

    /**
     * Creates a login controller.
     *
     * @param userService the user service to manage the users.
     * @param jwtService
     */
    @Autowired
    public LoginController(final UserService userService,
                           final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Check if the user can reach this api call.
     *
     * @param token the JWT token of the user that reached the url
     * @return a http response OK
     */
    @GetMapping("healthcheck")
    public ResponseEntity<Void> healthCheckLogin(
            final UsernamePasswordAuthenticationToken token) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Jwt token of {} verified", token.getName());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * The url to login on for users on a website.
     *
     * @param principal The user that logged in on Azure AD
     * @param response  The response that will be sent back to the user
     * @return a response entity
     */
    @GetMapping("login/web")
    public ResponseEntity<String> loginRedirectToWeb(
            @AuthenticationPrincipal final OAuth2User principal,
            final HttpServletResponse response) {
        String fullName = principal.getName();
        String email = principal.getAttribute("preferred_username");

        Authentication authentication = userService.login(email,
                fullName, email);
        String jwt = jwtService.generateToken(authentication);

        setCookieJSessionId(response);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} logged in on web and redirect",
                    principal.getName());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:19006/?JWT=" + jwt))
                .build();
    }

    /**
     * The url to login on for users on an Android application.
     *
     * @param principal The user that logged in on Azure AD
     * @param response  The response that will be sent back to the user
     * @return a response entity
     */
    @GetMapping("login/expo")
    public ResponseEntity<Void> loginRedirectToExpo(
            @AuthenticationPrincipal final OAuth2User principal,
            final HttpServletResponse response) {
        String email = principal.getAttribute("preferred_username");
        String fullName = principal.getName();
        Authentication authentication = userService.login(email,
                fullName, email);
        String jwt = jwtService.generateToken(authentication);
        setCookieJSessionId(response);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} logged in on expo and redirect",
                    principal.getName());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("exp://10.0.2.2:19000?JWT="
                        + jwt)).build();
    }

    /**
     * Set the cookie to nothing so that the login
     * with after the first time does not crash.
     *
     * @param response the response that will be sent back to the user.
     */
    private void setCookieJSessionId(final HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
