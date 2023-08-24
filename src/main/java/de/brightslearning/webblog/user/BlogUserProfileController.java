package de.brightslearning.webblog.user;

import de.brightslearning.webblog.media.LocalImageStorageService;
import de.brightslearning.webblog.session.Session;
import de.brightslearning.webblog.session.SessionRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogUserProfileController {

    private final LocalImageStorageService localImageStorageService;

    private final BlogUserRepository blogUserRepository;

    private final SessionRepository sessionRepository;

    @Autowired
    public BlogUserProfileController(LocalImageStorageService localImageStorageService, BlogUserRepository blogUserRepository, SessionRepository sessionRepository) {
        this.localImageStorageService = localImageStorageService;
        this.blogUserRepository = blogUserRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        // Retrieve user details and set to model attributes
        return "profile";
    }

    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture, Model model) {
        // First, store the image
        String storedFileName = localImageStorageService.store(profilePicture);
        System.out.println(storedFileName);

        // Now, you might want to associate this filename with the user in your database.
        // Assuming 'sessionUser' is the currently logged-in user and you have a 'setProfilePicture'
        // method on your user entity to set the filename of the profile picture.

        BlogUser sessionUser = (BlogUser) model.getAttribute("sessionUser");
        if (sessionUser != null) {
            sessionUser.setProfilePicturePath(storedFileName);
            System.out.println(storedFileName);
            // Save the user back to the database
            // Assuming you have 'blogUserRepository' bean initialized and injected
            blogUserRepository.save(sessionUser);
        }

        return "redirect:/profile";
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

    @PostMapping("/promoteUserByUsernameToAdmin")
    @Transactional
    public String promoteUserByUsernameToAdmin(@RequestParam String promoteUser, Model model) {
        Optional<BlogUser> optionalUserToDelete = Optional.ofNullable(blogUserRepository.findByUsername(promoteUser));

        if (optionalUserToDelete.isPresent() && optionalUserToDelete.get().getId() != 1) {
            BlogUser user = optionalUserToDelete.get();


            // Promote the user
            user.setAdmin(true);
            blogUserRepository.save(user);

            return "redirect:/profile";  // or wherever you want to redirect after deletion
        } else {
            System.out.println("cant delete admin numero uno");
        }

        // Maybe add an error message if user wasn't found
        model.addAttribute("errorMsg", "User not found!");

        return "redirect:/profile"; // or return to a specific page with an error message
    }

    @PostMapping("/demoteUserByUsernameToAdmin")
    @Transactional
    public String demoteUserByUsernameToAdmin(@RequestParam String demoteUser, Model model) {
        Optional<BlogUser> optionalUserToDelete = Optional.ofNullable(blogUserRepository.findByUsername(demoteUser));

        if (optionalUserToDelete.isPresent() && optionalUserToDelete.get().getId() != 1) {
            BlogUser user = optionalUserToDelete.get();


            // Promote the user
            user.setAdmin(false);
            blogUserRepository.save(user);

            return "redirect:/profile";  // or wherever you want to redirect after deletion
        } else {
            System.out.println("cant demote admin numero uno");
        }

        // Maybe add an error message if user wasn't found
        model.addAttribute("errorMsg", "User not found!");

        return "redirect:/profile"; // or return to a specific page with an error message
    }




}
