package com.yenicilh.chatapp.user.repository;

import com.yenicilh.chatapp.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 *
 * <p>This interface extends {@link JpaRepository} to provide basic CRUD operations
 * and allows custom query methods to be defined for additional functionality.</p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Provide database interaction for {@link User} entity.</li>
 *   <li>Support basic CRUD operations and custom queries.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 * @since 2024-01-01
 */

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to search for
     * @return an {@link Optional} containing the user if found, or empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username the username to check for
     * @return {@code true} if a user with the given username exists, {@code false} otherwise
     */
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);


    @Query("select u from User u where u.username Like %:query% or u.email Like %:query%")
    List<User> searchUser(@Param("query") String query);

}
