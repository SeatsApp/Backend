package com.seatapp.filters;

import com.seatapp.usermanagement.services.JwtService;
import com.seatapp.usermanagement.services.JwtUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    /**
     * The service to authenticate and create JWT tokens.
     */
    private final JwtService jwtService;
    /**
     * The service to handle the login of user with JWT tokens.
     */
    private final JwtUserDetailsService userDetailsService;
    /**
     * The paths given will be excluded from the filter
     * if the url contains these paths.
     */
    private final List<String> excludedPathsFromFilter;

    /**
     * Create a JwtAuthorizationFilter too only implement the control
     * of JWT tokens on certain paths.
     *
     * @param authManager             the authorization manager
     * @param jwtService          the service which handles the
     *                                authentication and creation of
     *                                the JWT tokens
     * @param userDetailsService      the service that handles the calls
     *                                of the user with JWT tokens
     * @param excludedPathsFromFilter the paths on which the filter will
     *                                be excluded from the filter when
     *                                it is contained in the url
     */
    public JwtAuthorizationFilter(final AuthenticationManager authManager,
                                  final JwtService jwtService,
                                  final
                                  JwtUserDetailsService userDetailsService,
                                  final String... excludedPathsFromFilter) {
        super(authManager);
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.excludedPathsFromFilter =
                Arrays.stream(excludedPathsFromFilter).toList();
    }

    @Override
    protected final void doFilterInternal(final HttpServletRequest request,
                                          final HttpServletResponse response,
                                          final FilterChain filterChain)
            throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        boolean hasExcludedPath =
                excludedPathsFromFilter.stream().anyMatch(url::contains);

        if (!hasExcludedPath) {
            String jwt = jwtService.parseJwt(request);
            if (jwt == null || !jwtService.validateJwtToken(jwt)) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return;
            } else {
                String email = jwtService.getEmailFromJwtToken(jwt);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null,
                                userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
