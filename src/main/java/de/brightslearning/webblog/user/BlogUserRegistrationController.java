package de.brightslearning.webblog.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlogUserRegistrationController {

    private final BlogUserRepository blogUserRepository;

    @Autowired
    public BlogUserRegistrationController(BlogUserRepository blogUserRepository) {
        this.blogUserRepository = blogUserRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registration", new RegistrationDTO("", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registration") RegistrationDTO registration, BindingResult bindingResult) {

        if(!registration.getPassword1().equals(registration.getPassword2())) {
            bindingResult.addError(new FieldError("registration", "password2", "passwords are not matching"));
        }

        if(blogUserRepository.existsByUsername(registration.getUsername())) {
            bindingResult.addError(new FieldError("registration", "username", "username already in use"));
        }

        if(bindingResult.hasErrors()) {
            return "register";
        }

        // Everything okay with signing up!!!
        BlogUser blogUser = new BlogUser(registration.getUsername(), registration.getPassword1());
        blogUserRepository.save(blogUser);

        return "redirect:/login";
    }
}
