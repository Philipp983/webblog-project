package de.brightslearning.webblog.user;

import de.brightslearning.webblog.session.Session;
import de.brightslearning.webblog.session.SessionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogUserRegistrationController {

    private final BlogUserRepository blogUserRepository;

    private final SessionRepository sessionRepository;

    @Autowired
    public BlogUserRegistrationController(BlogUserRepository blogUserRepository, SessionRepository sessionRepository) {
        this.blogUserRepository = blogUserRepository;
        this.sessionRepository = sessionRepository;
    }




    @GetMapping("/profile")
    public String profilePage(){
        return "profile";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registration", new RegistrationDTO("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationDTO registration, BindingResult bindingResult) {

        if (!registration.getPassword1().equals(registration.getPassword2())) {
            bindingResult.addError(new FieldError("registration", "password2", "passwords are not matching"));
        }

        if (blogUserRepository.existsByUsername(registration.getUsername())) {
            bindingResult.addError(new FieldError("registration", "username", "username already in use"));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Everything okay with signing up!!!
        BlogUser blogUser = new BlogUser(registration.getUsername(), registration.getPassword1());
        blogUserRepository.save(blogUser);

        return "redirect:/login";
    }

    @Transactional
    @PostMapping("/deleteAccount")
    public String deleteAccount(Model model) {
        BlogUser blogUser = (BlogUser) model.getAttribute("sessionUser");


        if (blogUser != null && blogUser.getId() != 1) {
            List<Session> sessionsForUser = sessionRepository.findByBlogUser(blogUser);
            for(Session s : sessionsForUser) {
                sessionRepository.delete(s);
            }
            blogUserRepository.delete(blogUser);
//            return "redirect:/logout";

        } else if (blogUser != null && blogUser.getId() == 1) {
            return "redirect:/profile";
        }
        return "redirect:/login";
    }

    @PostMapping("/deleteUserByUsername")
    @Transactional
    public String deleteUserByUsername(@RequestParam String userToDelete, Model model) {
        Optional<BlogUser> optionalUserToDelete = Optional.ofNullable(blogUserRepository.findByUsername(userToDelete));

        if (optionalUserToDelete.isPresent() && optionalUserToDelete.get().getId() != 1) {
            BlogUser user = optionalUserToDelete.get();

            // Delete all sessions for this user
            List<Session> sessionsForUser = sessionRepository.findByBlogUser(user);
            for(Session s : sessionsForUser) {
                sessionRepository.delete(s);
            }

            // Delete the user
            blogUserRepository.delete(user);

            return "redirect:/profile";  // or wherever you want to redirect after deletion
        } else {
            System.out.println("cant delete admin numero uno");
        }

        // Maybe add an error message if user wasn't found
        model.addAttribute("errorMsg", "User not found!");

        return "redirect:/profile"; // or return to a specific page with an error message
    }


}




