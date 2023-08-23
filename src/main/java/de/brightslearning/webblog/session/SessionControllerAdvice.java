package de.brightslearning.webblog.session;

import de.brightslearning.webblog.user.BlogUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Instant;
import java.util.Optional;

@ControllerAdvice
public class SessionControllerAdvice {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionControllerAdvice(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @ModelAttribute("sessionUser")
    public BlogUser sessionUser(@CookieValue(value = "sessionId", defaultValue = "") String sessionId) {
        if (!sessionId.isEmpty()) {
            Optional<Session> optionalSession = sessionRepository.findByIdAndExpiresAtAfter(
                    sessionId, Instant.now());
            if (optionalSession.isPresent()) {
                Session session = optionalSession.get();

                // new expiresAt value for the current session
                session.setExpiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60));
                sessionRepository.save(session);

                return session.getBlogUser();
            }
        }
        return null;
    }
}

