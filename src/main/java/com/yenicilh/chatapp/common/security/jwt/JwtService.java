package com.yenicilh.chatapp.common.security.jwt;

import com.yenicilh.chatapp.user.entity.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing JWT operations such as token creation, validation, and parsing.
 */
@Service
public class JwtService {

    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationMillis;

    /**
     * Generates a JWT token with custom claims.
     *
     * @param userName The username to include in the token.
     * @return A signed JWT token.
     */
    public String generateToken(String userName) {
        return generateToken(new HashSet<>(), userName);
    }

    /**
     * Generates a JWT token with specified claims and username.
     *
     * @param roles   Custom claims to include in the token.
     * @param userName The username to include in the token.
     * @return A signed JWT token.
     */
    public String generateToken(Set<Role> roles, String userName) {
        Map<String, Object> claims = roles.stream()
                .collect(Collectors.toMap(Role::name, role -> role.name()));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a given JWT token against the provided UserDetails.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The UserDetails to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUser(token);
            final Date expirationDate = extractExpiration(token);
            return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception for debugging purposes (e.g., token tampering, expired token)
            System.err.println("JWT validation error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username contained in the token.
     */
    public String extractUser(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return A Claims object containing all the claims.
     */
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token is expired");
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    /**
     * Generates a signing key from the secret key.
     *
     * @return A Key object for signing the JWT.
     */
    private Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid secret key configuration");
        }
    }
}
