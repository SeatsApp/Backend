package com.seatapp.usermanagement.services;

import com.seatapp.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    /**
     * Log the user in to the application.
     *
     * @param principal the user trying to log in
     * @return the authentication of the user
     */
    Authentication login(OAuth2User principal);

    /**
     * Set the cookie to nothing so that the login
     * with after the first time does not crash.
     *
     * @param response the response that will be sent back to the user.
     */
    void setCookieJSessionId(HttpServletResponse response);

    /**
     * Get role from logged in user.
     *
     * @param principal the user that logged in
     * @return the role of the user
     */
    Role getRole(OAuth2User principal);
}
