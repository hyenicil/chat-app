package com.yenicilh.chatapp.common.security.config;

import com.yenicilh.chatapp.common.security.jwt.JwtAuthFilter;
import com.yenicilh.chatapp.common.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Security configuration class for handling authentication and authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain for handling HTTP requests and security features.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain bean.
     * @throws Exception If a configuration error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors  // Add CORS configuration
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(List.of("http://localhost:3000")); // Or your frontend URL
                            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                            config.setAllowedHeaders(List.of("*")); // Or specific headers
                            config.setAllowCredentials(true);
                            return config;
                        })
                )
                .csrf(csrf -> csrf.disable()) // CSRF devre dışı
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/auth/generateToken/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/auth/user").hasRole("USER") // USER role
                        .requestMatchers("/auth/admin").hasRole("ADMIN") // ADMIN role
                        .anyRequest().authenticated() // Diğer tüm endpoint'ler için kimlik doğrulama gerekli
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .authenticationProvider(authenticationProvider()) // Özelleştirilmiş doğrulama
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // JWT doğrulama filtresi
                .build();
    }

    /**
     * Configures the DAO-based authentication provider.
     *
     * @return The configured AuthenticationProvider bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the authentication manager bean.
     *
     * @param authConfig The authentication configuration.
     * @return The configured AuthenticationManager bean.
     * @throws Exception If a configuration error occurs.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    /**
     * Provides a PasswordEncoder bean to the Spring context.
     *
     * @return an instance of BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
