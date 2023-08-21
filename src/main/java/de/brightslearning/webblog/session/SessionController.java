package de.brightslearning.webblog.session;

import de.brightslearning.webblog.user.BlogUser;
import de.brightslearning.webblog.user.BlogUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Instant;
import java.util.Optional;

@Controller
public class SessionController {

    private final SessionRepository sessionRepository;

    private final BlogUserRepository blogUserRepository;


    @Autowired
    public SessionController(SessionRepository sessionRepository, BlogUserRepository blogUserRepository) {
        this.sessionRepository = sessionRepository;
        this.blogUserRepository = blogUserRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new LoginDTO("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login") LoginDTO login, BindingResult bindingResult, HttpServletResponse response) {
        Optional<BlogUser> optionalUser = blogUserRepository.findByUsernameAndPassword(login.getUsername(), login.getPassword());

        if (optionalUser.isPresent()) {
            Session session = new Session(optionalUser.get(), Instant.now().plusSeconds(7 * 24 * 60 * 60)); //expires one week from now
            sessionRepository.save(session);


            //store the session ID in a cookie to keep the username secret
            Cookie cookie = new Cookie("sessionId", session.getId().toString());
            response.addCookie(cookie);

            // Login successful
            return "redirect:/";
        }

        bindingResult.addError(new FieldError("login", "password", "Login not successful."));

        return "login";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = "sessionId", defaultValue = "") String sessionId, HttpServletResponse response) {
        Optional<Session> optionalSession = sessionRepository.findByIdAndExpiresAtAfter(sessionId, Instant.now());
        optionalSession.ifPresent(session -> sessionRepository.delete(session));

        Cookie cookie = new Cookie("sessionId", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
