package com.yenicilh.chatapp.common.security.service;

import com.yenicilh.chatapp.user.entity.User;
import com.yenicilh.chatapp.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.withUsername;

/**
 * Custom implementation of UserDetailsService that loads user data from the database.
 * Integrates with Spring Security to provide authentication and authorization capabilities.
 *
 * <p>This class retrieves user details from the repository and maps them to Spring Security's
 * {@link UserDetails} object for proper authentication handling.</p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Fetch user details from the database based on the username.</li>
 *   <li>Map the user data to Spring Security's UserDetails model.</li>
 *   <li>Handle exceptions when the user is not found.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2024-01-01
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new instance of {@code CustomUserDetailsService}.
     *
     * @param userRepository the repository to access user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username.
     *
     * <p>The returned {@link UserDetails} object contains the user's credentials
     * and authorities, which are used for authentication and authorization.</p>
     *
     * @param username the username identifying the user whose data is required
     * @return a fully populated {@link UserDetails} object
     * @throws UsernameNotFoundException if the user could not be found or has no granted authorities
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", username)));
        UserBuilder builder = withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getAuthorities().toArray(new String[0]));
        return builder.build();
    }

    /**
     * Retrieves additional user information for auditing or business logic.
     *
     * @param username the username identifying the user
     * @return the full {@link User} entity
     * @throws UsernameNotFoundException if the user is not found
     */
    public User getUserDetails(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username '%s' not found", username)));
    }
}
