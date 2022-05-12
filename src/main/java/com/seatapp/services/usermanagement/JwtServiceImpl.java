package com.seatapp.services.usermanagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.seatapp.domain.usermanagement.JwtUserDetails;
import com.seatapp.services.LoggerService;
import com.seatapp.services.LoggerServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Jwts;

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
        JwtUserDetails userPrincipal =
                (JwtUserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userPrincipal.getUsername());
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
}
