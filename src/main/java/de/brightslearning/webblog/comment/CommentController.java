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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

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
            return "redirect:/";
        }
//        entry.setContent(entry.getContent().replace("<br>", ""));
        entry.setContent(entry.getContent().replace("\n", "<br>"));
        model.addAttribute("blogEntry", entry);
//        model.addAttribute("comment", new Comment()); // Add an empty comment to the model, but forgot why so no free models
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
            return "redirect:/";
        }
        //issue seems to be this block right here. without the
        Comment anotherComment = new Comment();
        anotherComment.setBlogUser(currentUser);
        anotherComment.setBlogEntry(entry);
        anotherComment.setContent(comment.getContent());

//        comment.setBlogUser(currentUser);
//        comment.setBlogEntry(entry); // Associate the comment with the blog entry

        commentRepository.save(anotherComment); // Save the comment to the database

        return "redirect:/comment/" + id + "#comment-" + anotherComment.getId(); // Redirect back to the comment page
    }

    @GetMapping("/editComment/{entryId}/{commentId}")
    public String showEditCommentForm(@PathVariable Integer entryId, @PathVariable Integer commentId, Model model, RedirectAttributes redirectAttributes) {
        Comment commentToEdit = commentRepository.findById(commentId).orElse(null);
        if (commentToEdit == null) {
            // Handle the case when the entry is not found
            return "redirect:/";
        }
        model.addAttribute("commentToEdit", commentToEdit);
        redirectAttributes.addFlashAttribute("commentToEdit", commentToEdit);

        // Code to display the edit comment form
        return "redirect:/comment/" + entryId;
    }

    @PostMapping("/editComment/{entryId}/{commentId}")
    public String editComment(@PathVariable Integer entryId, @PathVariable Integer commentId, @ModelAttribute Comment editedComment) {
        // Code to edit the comment
        Comment originalComment = commentRepository.findById(commentId).orElse(null);

        if(originalComment == null) {
            // Handle the case where the original comment doesn't exist
            return "redirect:/";
        }

//        originalComment.setId((editedComment.getId()));
        originalComment.setLastEdited(LocalDateTime.now());
        originalComment.setContent(editedComment.getContent());
        // Add any other fields to update, if necessary.

        commentRepository.save(originalComment); // Assuming you have a save method in your repository.

        return "redirect:/comment/" + entryId + "#comment-" + commentId; // Redirect back to the comment page to display the updated comment.
    }

    @GetMapping("/deleteComment/{entryId}/{commentId}")
    public String deleteComment(@PathVariable Integer entryId, @PathVariable Integer commentId) {
        // Code to delete the comment
        commentRepository.deleteById(commentId);
        System.out.println(entryId);
        System.out.println(commentId);
        return "redirect:/comment/" + entryId;
    }

    @GetMapping("/setAsDeleted/{entryId}/{commentId}")
    public String setAsDeleted(@PathVariable Integer entryId, @PathVariable Integer commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setDeleted(true);  // assuming you have a setter named setIsDeleted in your Comment entity
            commentRepository.save(comment);
        }

        System.out.println(entryId);
        System.out.println(commentId);
        return "redirect:/comment/" + entryId + "#comment-" + commentId;
    }

    @GetMapping("/restoreDeleted/{entryId}/{commentId}")
    public String restoreDeleted(@PathVariable Integer entryId, @PathVariable Integer commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setDeleted(false);  // assuming you have a setter named setIsDeleted in your Comment entity
            commentRepository.save(comment);
        }

        System.out.println(entryId);
        System.out.println(commentId);
        return "redirect:/comment/" + entryId + "#comment-" + commentId;
    }
}