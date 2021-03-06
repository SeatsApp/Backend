package com.seatapp.controllers;

import com.seatapp.usermanagement.services.JwtService;
import com.seatapp.usermanagement.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
     * Represents the login service.
     */
    private final LoginService loginService;

    /**
     * Represents the user service.
     */
    private final JwtService jwtService;

    /**
     * Redirect to expo web but on production to admin web.
     */
    @Value("${redirect.web}")
    private String redirectUrlWeb;

    /**
     * Redirect to the expo app.
     */
    @Value("${redirect.expo}")
    private String redirectUrlExpo;

    /**
     * Creates a login controller.
     *
     * @param loginService the login service to manage the users.
     * @param jwtService   represents the jwt service
     */
    @Autowired
    public LoginController(final LoginService loginService,
                           final JwtService jwtService) {
        this.loginService = loginService;
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
        Authentication authentication = loginService.login(principal);
        String jwt = jwtService.generateToken(authentication);

        loginService.setCookieJSessionId(response);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} logged in on web and redirect",
                    principal.getName());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrlWeb + "?JWT=" + jwt))
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
        Authentication authentication = loginService.login(principal);
        String jwt = jwtService.generateToken(authentication);

        loginService.setCookieJSessionId(response);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} logged in on expo and redirect",
                    principal.getName());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrlExpo + "?JWT="
                        + jwt)).build();
    }
}
