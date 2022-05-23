package com.seatapp.filters;

import com.seatapp.domain.Role;
import com.seatapp.usermanagement.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminFilter extends BasicAuthenticationFilter {
    /**
     * The service to authenticate and create JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * First the http method with a space and then
     * the path on which the filtering will be done.
     * <p>
     * Example: POST /api/seats
     */
    private final List<String> includedHttpMethodAndPathForFiltering;

    /**
     * Creates the admin filter.
     *
     * @param authenticationManager            represents
     *                                         the authorization manager
     * @param jwtService                       the service which handles the
     *                                         authentication and creation of
     *                                         the JWT tokens
     * @param incHttpMethodAndPathForFiltering first the http method with
     *                                         a space and then
     *                                         the paths on which the filtering
     *                                         will be done
     *                                         <p>
     *                                         Example: POST /api/seats
     */
    public AdminFilter(
            final AuthenticationManager authenticationManager,
            final JwtService jwtService,
            final String... incHttpMethodAndPathForFiltering) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.includedHttpMethodAndPathForFiltering =
                Arrays.stream(incHttpMethodAndPathForFiltering).toList();
    }

    @Override
    protected final void doFilterInternal(final HttpServletRequest request,
                                          final HttpServletResponse response,
                                          final FilterChain filterChain)
            throws IOException, ServletException {
        String url = request.getRequestURL().toString();

        boolean includedInFilter = false;

        for (String httpMethodAndPath : includedHttpMethodAndPathForFiltering) {
            String[] httpMethodAndPathSplit = httpMethodAndPath.split(" ");
            String httpMethod = httpMethodAndPathSplit[0];
            String path = httpMethodAndPathSplit[1];

            if (url.contains(path) && request.getMethod().equals(httpMethod)) {
                includedInFilter = true;
                break;
            }
        }

        if (includedInFilter) {
            String jwt = jwtService.parseJwt(request);
            Role role = jwtService.getRoleFromJwtToken(jwt);

            if (!role.equals(Role.ADMIN)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
