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
@RequestMapping("api/admin")
public class AdminLoginController {
    /**
     * The logger of the login controller.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AdminLoginController.class);

    /**
     * Represents the login service.
     */
    private final LoginService loginService;

    /**
     * Represents the user service.
     */
    private final JwtService jwtService;

    /**
     * Redirect to admin web.
     */
    @Value("${redirect.admin.web}")
    private String redirectUrlAdminWeb;

    /**
     * Creates a login controller.
     *
     * @param loginService the login service to manage the users.
     * @param jwtService   represents the JWT service
     */
    @Autowired
    public AdminLoginController(
            final LoginService loginService,
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
     * The url to login on for admins on the website.
     *
     * @param principal The user that logged in on Azure AD
     * @param response  The response that will be sent back to the user
     * @return a response entity
     */
    @GetMapping("login")
    public ResponseEntity<String> loginRedirectToAdminWeb(
            @AuthenticationPrincipal final OAuth2User principal,
            final HttpServletResponse response) {
        Authentication authentication = loginService.login(principal);
        String jwt = jwtService.generateToken(authentication);

        loginService.setCookieJSessionId(response);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} logged in on admin web and redirect",
                    principal.getName());
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrlAdminWeb + "?JWT=" + jwt))
                .build();
    }
}
