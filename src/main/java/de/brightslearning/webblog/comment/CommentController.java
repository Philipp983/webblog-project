package de.brightslearning.webblog.comment;

import de.brightslearning.webblog.blogentry.BlogEntry;
import de.brightslearning.webblog.blogentry.BlogEntryRepository;
import de.brightslearning.webblog.user.BlogUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/comment/{id}")
    public String showCommentPage(@PathVariable Integer id, Model model) {
        // Find the blog entry by ID
        BlogEntry entry = blogEntryRepository.findById(id).orElse(null);

        if (entry == null) {
            // Handle the case when the entry is not found, e.g., redirect to an error page
            return "error";
        }

        model.addAttribute("blogEntry", entry);
        model.addAttribute("comment", new Comment()); // Add an empty comment to the model
        model.addAttribute("previousComments", entry.getComments()); // Adding list of previous comments to the model

        return "comment"; // Name of your comment page HTML (e.g., comment.html)
    }

    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable Integer id, @ModelAttribute("comment") Comment comment, Model model) {
        BlogEntry entry = blogEntryRepository.findById(id).orElse(null);
        BlogUser currentUser = (BlogUser) model.getAttribute("sessionUser");
        if(currentUser == null) {
            // The user is not logged in or the session expired. Handle this case appropriately.
            // Maybe redirect to the login page or show an error message.
            return "redirect:/login";
        }
        if (entry == null) {
            // Handle the case when the entry is not found
            return "error";
        }
        comment.setBlogUser(currentUser);
        comment.setBlogEntry(entry); // Associate the comment with the blog entry

        commentRepository.save(comment); // Save the comment to the database

        return "redirect:/comment/" + id; // Redirect back to the comment page
    }
}