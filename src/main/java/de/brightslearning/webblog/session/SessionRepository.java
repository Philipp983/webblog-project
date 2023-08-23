package de.brightslearning.webblog.session;

import de.brightslearning.webblog.user.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByIdAndExpiresAtAfter(String id, Instant expiresAt);
    List<Session> findByBlogUser(BlogUser user);
}
