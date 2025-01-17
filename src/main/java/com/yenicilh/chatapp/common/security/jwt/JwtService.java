package com.yenicilh.chatapp.common.security.jwt;

import com.yenicilh.chatapp.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.ResourceTransactionManager;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing JWT operations: creation, validation, and parsing.
 */
@Service
public class JwtService {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generates a JWT token for a given username.
     *
     * @param username The username for which the token is generated.
     * @return A signed JWT token.
     */
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    /**
     * Generates a JWT token with custom claims and username.
     *
     * @param claims Custom claims to include in the token.
     * @param username The username for which the token is generated.
     * @return A signed JWT token.
     */
    public String generateToken(Map<String,Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token based on authentication details.
     *
     * @param authentication Authentication object.
     * @return A signed JWT token.
     */
    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName());
    }

    /**
     * Generates a JWT token for a given User object.
     *
     * @param user The User object containing the user's information.
     * @return A signed JWT token.
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getAuthorities());
        claims.put("profilePictureUrl", user.getProfilePictureUrl());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token agains the provided UserDetails.
     *
     * @param token       The JWT token.
     * @param userDetails The UserDetails for validation.
     * @return True if valid, otherwise false.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return userDetails.getUsername().equals(username) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
        return false;
        }
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     *Checks if the token is expired
     *
     * @param token The JWT token.
     * @return True if expired, otherwise false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the Jwt token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return A Claims object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(removeBearerPrefix(token))
                    .getBody();
        }catch (ExpiredJwtException e) {
            throw new IllegalStateException("Token has expired.", e);
        } catch (JwtException e) {
            throw new IllegalStateException("Invalid token.", e);
        }
    }

    /**
     * Removes the 'Bearer ' prefix from the token if present.
     *
     * @param token The token with or without the prefix.
     * @return The token without the prefix.
     */
    private String removeBearerPrefix(String token) {
        if(token.startsWith("Bearer "))
            return token.substring(7);
        return token;
    }

    /**
     * Retrives the signing key used for JWT operations.
     *
     * @return A Key object for signing the JWT.
     */
    private Key getSigningKey() {
       byte[] keyBytes = Decoders.BASE64.decode(key);
       return Keys.hmacShaKeyFor(keyBytes);
    }



}
