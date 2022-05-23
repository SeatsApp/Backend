package com.seatapp.usermanagement.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seatapp.domain.Role;
import com.seatapp.services.LoggerService;
import com.seatapp.services.LoggerServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Locale;


@Component
public class JwtServiceImpl implements JwtService {
    /**
     * The JWT secret.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * The JWT expiration time in milliseconds.
     */
    @Value("${jwt.timeout}")
    private int jwtExpirationMs;

    /**
     * The logger for JwtUtils.
     */
    private final LoggerService loggerService =
            new LoggerServiceImpl(getClass());

    /**
     * Generates a JWT token.
     *
     * @param authentication the authentication on which
     *                       the JWT will be gennerated
     * @return a JWT token
     */
    @Override
    public String generateToken(final Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authentication.getAuthorities().toArray()[0]);
        return doGenerateToken(claims, authentication.getName());
    }

    /**
     * Generate a JWT token using the jwts builder.
     *
     * @param claims  the roles of the user
     * @param subject the email of the user
     * @return a JWT token
     */
    private String doGenerateToken(final Map<String, Object> claims,
                                   final String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    /**
     * Get email by JWT token.
     *
     * @param token the JWT token
     * @return the email from the JWT token
     */
    @Override
    public String getEmailFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get role by JWT token.
     *
     * @param token the JWT token
     * @return the role from the JWT token
     */
    @Override
    public Role getRoleFromJwtToken(final String token) {
        Object authority = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token).getBody().get("role");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> authorityMap =
                objectMapper.convertValue(authority,
                        new TypeReference<
                                LinkedHashMap<String, String>>() {
                        });
        String role = authorityMap.get("authority");
        return Role.valueOf(role.toUpperCase(Locale.ROOT));
    }

    /**
     * Validate the JWT token.
     *
     * @param token the JWT token
     * @return if the JWT token is validated
     */
    @Override
    public boolean validateJwtToken(final String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            String message = "Invalid JWT signature: " + e.getMessage();
            loggerService.debug(message);
        } catch (MalformedJwtException e) {
            String message = "Invalid JWT token: " + e.getMessage();
            loggerService.debug(message);
        } catch (ExpiredJwtException e) {
            String message = "JWT token is expired: " + e.getMessage();
            loggerService.debug(message);
        } catch (UnsupportedJwtException e) {
            String message = "JWT token is unsupported: " + e.getMessage();
            loggerService.debug(message);
        } catch (IllegalArgumentException e) {
            String message = "JWT claims string is empty: "
                    + e.getMessage();
            loggerService.debug(message);
        }

        return false;
    }

    /**
     * retrieves the JWT token out of the http request.
     *
     * @param request the request from which the JWT token
     *                will be retrieved.
     * @return the parsed JWT token
     */
    @Override
    public String parseJwt(final HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        String startOfAuthorization = "Bearer ";
        if (StringUtils.hasText(headerAuth)
                && headerAuth.startsWith(startOfAuthorization)) {
            return headerAuth.substring(startOfAuthorization.length());
        }

        return null;
    }
}
