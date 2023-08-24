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
public class ChangePasswordController {

    private final BlogUserRepository blogUserRepository;

    @Autowired
    public ChangePasswordController(BlogUserRepository blogUserRepository) {
        this.blogUserRepository = blogUserRepository;
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        BlogUser currentUser = (BlogUser) model.getAttribute("sessionUser");
        model.addAttribute("changePassword", new ChangePasswordDTO(currentUser.getUsername(), "","",""));
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePassword") ChangePasswordDTO changePassword,
                                 BindingResult bindingResult, Model model) {

        BlogUser currentUser = (BlogUser) model.getAttribute("sessionUser");

        if (!currentUser.getPassword().equals(changePassword.getCurrentPassword())) {
            bindingResult.addError(new FieldError("changePassword", "currentPassword", "Invalid current password"));
            return "change-password";
        }

        // Check if the new password and confirm password match
        if (!changePassword.getNewPassword1().equals(changePassword.getNewPassword2())) {
            bindingResult.addError(new FieldError("changePassword", "newPassword2", "New passwords do not match"));
            return "change-password";
        }


        // Update the password
        currentUser.setPassword(changePassword.getNewPassword1());
        blogUserRepository.save(currentUser);

        return "redirect:/login";
    }
}
