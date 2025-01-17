package com.yenicilh.chatapp.common.security.jwt;

import com.yenicilh.chatapp.common.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Method to validate the JWT token from the request.
     * This filter intercepts the request, extracts the token and validates it.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param chain The filter chain
     * @throws ServletException If an error occurs during request processing
     * @throws IOException If an error occurs during I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        // Check if the Authorization header is present and starts with "Bearer"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Remove the "Bearer " prefix
            username = jwtService.extractUsername(jwtToken); // Extract username from the token
        }

        // If the username is valid and the authentication is not set yet in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load the user details using the username
                UserDetails user = userDetailsService.loadUserByUsername(username);

                // Validate the token
                if (jwtService.validateToken(jwtToken, user)) {
                    // If the token is valid, create an authentication token
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    // Set the authentication details (HTTP request information)
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // If an error occurs, log it and potentially send a 401 unauthorized response.
                logger.error("Authentication failed: ", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        // Proceed with the filter chain (passing the request and response along)
        chain.doFilter(request, response);
    }
}
