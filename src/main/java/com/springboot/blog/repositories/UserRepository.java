package com.springboot.blog.repositories;

import com.springboot.blog.entities.User;
import com.springboot.blog.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByUserRole(UserRole userRole);

    Boolean existsByEmail(String email);
}
