package edu.ntnu.idatt2106.g07.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

/**
 * Class dedicated to creating a JSON Web Token (JWT).
 */
@Component
public class JwtProvider {
    private final String secret = "59875c45-3404-4f4b-8d40-0db4d30588dd";
    private final long duration = Duration.ofHours(24).toMillis();

    /**
     * Generates a new token.
     * 
     * @param auth
     *            retrieves the authentication on a specified user.
     * 
     * @return a new token with expiry dates and signs with a key.
     */
    public String generateToken(Authentication auth) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        Date now = new Date();
        Date expiry = new Date(now.getTime() + duration);

        return Jwts.builder().setSubject(principal.getUsername()).setIssuedAt(now).setExpiration(expiry).signWith(key)
                .compact();
    }

    /**
     * Validates a specified token.
     * 
     * @param token
     *            retrieves a token.
     * 
     * @return validation successful if true, and unsuccessful if false.
     */
    public boolean validateToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the requested username from the specified token.
     * 
     * @param token
     *            retrieves a token.
     * 
     * @return the requested username.
     */
    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = parser.parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}
