package com.bluestaq.notes.repository;

import com.bluestaq.notes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);
    Optional<User> findByShareToken(String shareToken); // <-- Added for public read access
    Optional<User> findByEmail(String email);
}