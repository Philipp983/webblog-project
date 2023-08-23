package de.brightslearning.webblog.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByIdAndExpiresAtAfter(String id, Instant expiresAt);
}
